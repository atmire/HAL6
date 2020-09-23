- [Introduction](#Introduction)
- [Acknowledgements](#Acknowledgements)
- [Features](#Features)
	- [Affiliation lookup](#Affiliation-lookup)
	- [Submission UI roles](#Submission-UI-roles)
	- [HAL compliance step](#HAL-compliance-step)
	- [HAL sidebar link](#HAL-sidebar-link)
- [Patch Installation Procedures](#Patch-installation-procedures)
	- [Prerequisites](#Prerequisites)
	- [Obtaining a recent patch file](#Obtaining-recent-patch)
	- [Patch installation](#Patch-installation)
		- [1. Go to the DSpace Source directory.](#goto-DSpace-Source)
		- [2. Run the Git command to check whether the patch can be correctly applied.](#Run-git-command)
		- [3. Apply the patch](#Apply-patch)
		- [4. Rebuild and redeploy your repository](#Rebuild-redeploy)
		- [5. Restart your tomcat](#Restart-tomcat)
	- [Configure the metadata mapping](#Metadata-mapping)
- [Testing](#Testing)

# Introduction <a name="Introduction"></a>

The HAL patch adds support for item export to HAL in DSpace (designed for DSpace 6, developed and tested against DSpace 6.3).
For more information about HAL, please refer to https://hal.archives-ouvertes.fr/.

# Acknowledgements <a name="Acknowledgements"></a>

The HAL patch has been supported and funded by the University of Bordeaux (www.u-bordeaux.com) within the framework IdEx Bordeaux n° ANR 10-IDEX-03-02, and developed by Atmire (https://www.atmire.com/).

# Features <a name="Features"></a>

## Affiliation lookup <a name="Affiliation-lookup"></a>
A feature has been added to manage affiliations of authors in a new submission. This is done through a new dropdown menu which appears next to each author. This UI provides a search box to search for laboratories. Items which need to be sent to HAL need at least one author with an affiliated laboratory.

![Image not loading](/readme_images/author_affiliation.png?raw=true)

## Submission UI roles <a name="Submission-UI-roles"></a>
Authors can have a "role", this role can be chosen in the submission forms using the dropdown menu next an already added author.

![Image not loading](/readme_images/author_roles.png?raw=true)

## HAL compliance step <a name="HAL-compliance-step"></a>
One of the last steps in the item submission workflow will test if the non metadata requirements for the item to be sent to HAL are satisfied.

![Image not loading](/readme_images/hal_compliance.png?raw=true)

## HAL sidebar link <a name="HAL-sidebar-link"></a>
In the item view of an archived item, the sidebar contains a link to verify the HAL status. Click this link to see if an item has been sent to HAL. If the item isn't in HAL yet, you can upload it to HAl by using the popup.

# Patch Installation Procedures <a name="Patch-installation-procedures"></a>

## Prerequisites  <a name="Prerequisites"></a>

The HAL changes have been released as a "patch" for DSpace as this allows for the easiest installation process of the incremental codebase.

**__Important note__**: Hereunder, you will find guidance on how to apply the patch to your existing installation.
This will affect your source code. Before applying a patch, it is **always** recommended to create backup of your DSpace source code.

In order to apply the patch, you will need to locate the **DSpace source code** on your server. That source code directory contains a directory _dspace_, as well as the following files:  _LICENSE_,  _NOTICE_ ,  _README_ , ....

For every release of DSpace, generally two release packages are available. One package has "src" in its name and the other one does not. The difference is that the release labelled "src" contains ALL of the DSpace source code, while the other release retrieves precompiled packages for specific DSpace artifacts from maven central. 
**The HAL changes were designed to work on both "src" and other release packages of DSpace**. 

To be able to install the patch, you will need the following prerequisites:

* A running DSpace 6.3 instance. 
* Git should be installed on the machine. The patch will be applied using several git commands as indicated in the next section. 

## Obtaining a recent patch file <a name="Obtaining-recent-patch"></a>

Atmire's modifications to a standard DSpace are tracked on Github. 
The newest patch can therefore be generated from git.

DSPACE 6.3 [https://github.com/atmire/HAL6/compare/813800ce1736ec503fdcfbee4d86de836788f87c...master.diff](https://github.com/atmire/HAL6/compare/813800ce1736ec503fdcfbee4d86de836788f87c...master.diff)

## Patch installation <a name="Patch-installation"></a>

To install the patch, the following steps will need to be performed. 

### 1. Go to the DSpace Source directory. <a name="goto-DSpace-Source"></a>

This folder should have a structure similar to:   
dspace  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   modules  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    config  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ...  
pom.xml


### 2. Run the Git command to check whether the patch can be correctly applied. <a name="Run-git-command"></a>

Run the following command where <patch file> needs to be replaced with the name of the patch:

``` 
git apply --exclude README.md --exclude *.png --check <patch file>
```

This command will return whether it is possible to apply the patch to your installation. This should pose no problems in case the DSpace is not customized or in case not many customizations are present.   
In case, the check is successful, the patch can be installed without any problems. Otherwise, you will have to merge some changes manually.

### 3. Apply the patch <a name="Apply-patch"></a>

To apply the patch, the following command should be run where <patch file> is replaced with the name of the patch file. 

``` 
git apply --exclude README.md --exclude *.png --whitespace=nowarn --reject <patch file>
```

This command will tell git to apply the patch and ignore unharmful whitespace issues. The `--reject` flag instructs the command to continue when conflicts are encountered and saves the corresponding code hunks to a `.rej` file so you can review and apply them manually later on. Before continuing to the next step, you have to resolve all merge conflicts indicated by the `.rej` files. After solving the merge conflicts, remove all the `.rej` files.

### 4. Rebuild and redeploy your repository <a name="Rebuild-redeploy"></a>

After the patch has been applied, the repository will need to be rebuild.   
DSpace repositories are typically built using the Maven and deployed using Ant. 

### 5. Restart your tomcat <a name="Restart-tomcat"></a>

After the repository has been rebuild and redeployed, the tomcat will need to be restarted to bring the changes to production.

## Configure the metadata mapping <a name="Metadata-mapping"></a>

Your HAL login credentials should be configured in the `hal.login.user` and `hal.login.pass` configuration parameters in `dspace/config/modules/hal.cfg`. The default values of the other configuration parameters in this file are set to use the HAL test servers. When moving to a production environment, these should be updated. To make this easy, a second set of the relevant parameters has been included in this file, and commented out. To configure the HAL patch to use the production environment, simply uncomment the block below & fill in the missing parameters.

```
# HAL.login.user = ****
# HAL.login.pass = ****
# HAL.v3.solr.server = http://api.archives-ouvertes.fr/ref/structure
# HAL.v3.solr.server.authors = http://api.archives-ouvertes.fr/search/authorstructure
# HAL.url = http://api.archives-ouvertes.fr/sword/
# HAL.link.url = https://hal.univ-lille.fr/
# HAL.v3.sword.deposit.url = https://api.archives-ouvertes.fr/sword/univ-lille
# HAL.on-behalf-of =
```

and comment out the testing version of these parameters. In the end, each of the listed parameters should only occur once in this configuration file.

The same file also contains the metadata mapping. In the example below

```
# HAL Label: Laboratory
hal.export.laboratory = dc.contributor.author
```

the HAL field "Laboratory" is linked to the "dc.contributor.author" dspace metadata field. Some of the HAL fields only take a specific set of values. The allowed values are documented on the HAL website. For all relevant fields, a link to this HAL website is provided in the configuration file. The list of allowed values can be used to generate a controlled vocabulary or value-pairs in the input-forms.

```
# HAL Label: Audience
# Possible values: https://api.archives-ouvertes.fr/ref/metadataList/?q=metaName_s:audience&fl=*&wt=xml
hal.export.audience = hal.audience
```

The above configuration contains an example where the "hal.audience" field is sent to the HAL field with label: "Audience", but HAL only accepts a limited list of values:
* 1
* 2
* 3

For these fields you don't have to store the integer in the metadata, the HAL patch provides some internal mapping as well for these fields. 
To see this mapping search for "hal.export.audience" in the "hal-sword-export.xml" file, it will look like this:
```xml
<bean class="com.atmire.dspace.hal.xmlgenerating.attributes.metadata.SingleValueMappedAttributeMetadataFieldGenerator">
    <property name="elementName" value="note"/>
    <property name="metadataField" value="${hal.export.audience}"/>
    <property name="attributeName" value="n"/>
    <property name="attributes">
        <bean class="com.atmire.dspace.hal.xmlgenerating.attributes.metadata.ValueAsAttributeGenerator">
            <property name="name" value="type"/>
            <property name="value" value="audience"/>
        </bean>
    </property>
    <property name="map">
        <map>
            <entry key="Non spécifiée" value="1"/>
            <entry key="Internationale" value="2"/>
            <entry key="Nationale" value="3"/>
        </map>
    </property>
</bean>
```                                            

The property "map" contains as a key the values that are possible as metadata values, the left contains the values that DSpace can have in the metadata, the "value" contains the value that is sent to HAL. 