package com.atmire.dspace.hal.script;

import com.atmire.dspace.hal.factory.HALServiceFactory;
import com.atmire.dspace.hal.service.HALDepositService;
import com.atmire.dspace.hal.swordapp.client.HALSWORDClient;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.factory.AuthorizeServiceFactory;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Item;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.factory.EPersonServiceFactory;
import org.dspace.eperson.service.EPersonService;
import org.dspace.versioning.VersionHistory;
import org.dspace.versioning.factory.VersionServiceFactory;
import org.dspace.versioning.service.VersionHistoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by jonas - jonas@atmire.com on 16/02/2018.
 */
public class SendNewVersionToHALScript {

    /* Log4j logger*/
    private static final Logger log = Logger.getLogger(SendNewVersionToHALScript.class);
    private static final String HAL_STATUS_SCHEMA = "hal";
    private static final String HAL_STATUS_ELEMENT = "update";
    private static final String HAL_UPDATE_ERROR_STATUS_ELEMENT = "update-error";
    private static final String HAL_STATUS_QUALIFIER = "status";

    private static final String PERSON_OPTION = "e";
    private static final String HELP_OPTION = "h";

    private ItemService itemService;
    private HALDepositService halDepositService;
    private VersionHistoryService versionHistoryService ;
    private EPersonService ePersonService;
    private AuthorizeService authorizeService;

    private Context context;

    public static void main(String... args) throws SQLException, IOException, AuthorizeException, ParseException {
        SendNewVersionToHALScript script = new SendNewVersionToHALScript();
        script.mainImpl(args);
    }

    private SendNewVersionToHALScript() {
        itemService = ContentServiceFactory.getInstance().getItemService();
        halDepositService = HALServiceFactory.getInstance().getHALDepositService();
        versionHistoryService = VersionServiceFactory.getInstance().getVersionHistoryService();
        ePersonService = EPersonServiceFactory.getInstance().getEPersonService();
        authorizeService = AuthorizeServiceFactory.getInstance().getAuthorizeService();
        context = new Context();
    }

    private void mainImpl(String... args) throws ParseException {
        try {
            processArguments(args);
            sendVersionUpdatesToHAL();
            retryFailedSubmissions(new String[]{HALSWORDClient.DepositType.NEW.getTypeString(), HALSWORDClient.DepositType.UPDATE.getTypeString()});
        } catch (AuthorizeException | IOException | SQLException e) {
            log.error(LogManager.getHeader(context, "hal_deposit_script_error", "Error while despositing items to HAL in bulk"), e);
        }
    }

    private void processArguments(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = createCommandLineOptions();
        CommandLine line = parser.parse(options, args);
        parseCommandLineOptions(options, line);
    }

    private void parseCommandLineOptions(Options options, CommandLine line) {
        if (line.hasOption(HELP_OPTION) || !line.hasOption(PERSON_OPTION)) {
            printHelp(options);
            System.exit(0);
        }
        String email = line.getOptionValue(PERSON_OPTION);
        try {
            EPerson ePerson = ePersonService.findByEmail(context, email);
            if(!authorizeService.isAdmin(context, ePerson)){
                noValidEPersonFound(options);
            }
            context.setCurrentUser(ePerson);
        } catch (SQLException e) {
            noValidEPersonFound(options);
        }
    }

    private void noValidEPersonFound(Options options) {
        System.out.println("Please provide a valid admin account");
        printHelp(options);
        System.exit(0);
    }

    private void sendVersionUpdatesToHAL() throws SQLException, AuthorizeException, IOException {
        retrieveItemsAndProcess(HAL_STATUS_ELEMENT, HALSWORDClient.DepositType.UPDATE.getTypeString(), "hal_deposit_script_send_version_update", "Starting HAL version update script");
    }

    private void retryFailedSubmissions(String... versionsToCheck) throws SQLException, AuthorizeException, IOException {
        log.info(LogManager.getHeader(context, "hal_deposit_script_retry", "Retrying previously failed HAL deposits"));

        for (String versionToCheck : versionsToCheck) {
            retrieveItemsAndProcess(HAL_UPDATE_ERROR_STATUS_ELEMENT, versionToCheck, "hal_deposit_script_retry_"+versionToCheck, "Retrying previously failed HAL deposits for status: "+versionToCheck);
        }
    }


    private void retrieveItemsAndProcess(String elementName, String value, String headerStatus, String headerMessage) throws SQLException, AuthorizeException, IOException {
        Iterator<Item> itemIterator = itemService.findByMetadataField(context, HAL_STATUS_SCHEMA, elementName, HAL_STATUS_QUALIFIER, value);
        log.info(LogManager.getHeader(context, headerStatus, headerMessage));

        int count = 0;
        while (itemIterator.hasNext()) {
            Item next = itemIterator.next();
            processItem(next);
            itemService.clearMetadata(context, next, HAL_STATUS_SCHEMA, elementName, HAL_STATUS_QUALIFIER, Item.ANY);
            itemService.update(context, next);
            count++;
            if (count % 100 == 0) {
                context.commit();
            }
        }
        context.commit();
    }
    private void processItem(Item item) throws SQLException, AuthorizeException, IOException {
        if(!isLastVersion(item)){
            log.info(LogManager.getHeader(context, "hal_deposit_script_not_Latest", "Skipping deposit for item because it is not the latest version: " + item.getHandle()));
            return;
        }
        boolean isValidHalItem = halDepositService.isValidHalItem(context, item);
        if (!isValidHalItem) {
            log.info(LogManager.getHeader(context, "hal_deposit_script_invalid", "Skipping deposit for item because it is found to not be valid: " + item.getHandle()));
        }
        boolean metadataOnly = false;
        halDepositService.depositSubmissionToHal(context, item, metadataOnly);
    }

    private boolean isLastVersion(Item item) throws SQLException {
        VersionHistory history = versionHistoryService.findByItem(context, item);
        if(history!=null){
            if(!versionHistoryService.getLatestVersion(context, history).getItem().equals(item)){
                return false;
            }
        }
        return true;
    }

    private Options createCommandLineOptions() {
        Options options = new Options();
        options.addOption(PERSON_OPTION, "person", true, "The email-address of the person running the script.");
        options.addOption(HELP_OPTION, "help", false, "Print the usage of the script");
        return options;
    }

    private void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("dsrun " + getClass().getCanonicalName(), options);
    }
}
