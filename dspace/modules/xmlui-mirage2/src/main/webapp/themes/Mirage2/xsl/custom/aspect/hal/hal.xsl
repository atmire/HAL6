<!--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

-->

<!--
    Templates to cover the forms and forms fields.

    Author: art.lowel at atmire.com
    Author: lieven.droogmans at atmire.com
    Author: ben at atmire.com
    Author: Alexey Maslov

-->

<xsl:stylesheet xmlns:i18n="http://apache.org/cocoon/i18n/2.1"
                xmlns:dri="http://di.tamu.edu/DRI/1.0/"
                xmlns:mets="http://www.loc.gov/METS/"
                xmlns:xlink="http://www.w3.org/TR/xlink/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:dim="http://www.dspace.org/xmlns/dspace/dim"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:mods="http://www.loc.gov/mods/v3"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:confman="org.dspace.core.ConfigurationManager"
                exclude-result-prefixes="i18n dri mets xlink xsl dim xhtml mods dc confman">

    <xsl:output indent="yes"/>

    <xsl:template match="//dri:item[@n='halStatus']">
        <xsl:variable name="itemID">
            <xsl:value-of select="//dri:item[@n='itemID']"/>
        </xsl:variable>
        <xsl:variable name="retrievedStatus">
            <xsl:value-of select="//dri:item[@n='retrievedStatus']"/>
        </xsl:variable>
        <xsl:variable name="identifier">
            <xsl:value-of select="//dri:item[@n='halIdentifier']"/>
        </xsl:variable>
        <xsl:variable name="version">
            <xsl:value-of select="//dri:item[@n='halVersion']"/>
        </xsl:variable>
        <xsl:variable name="error">
            <xsl:value-of select="//dri:item[@n='halError']"/>
        </xsl:variable>
        <xsl:variable name="halCompliance">
            <xsl:value-of select="//dri:item[@n='halCompliance']"/>
        </xsl:variable>
        <xsl:variable name="halComplianceLink">
            <xsl:value-of select="//dri:item[@n='halComplianceLink']"/>
        </xsl:variable>
        <xsl:variable name="halLinkBase">
            <xsl:value-of select="confman:getProperty('hal.link.url')"/>
        </xsl:variable>
        <div class="modal hal-status-modal">
            <xsl:if test="$error">
                <xsl:attribute name="class">modal hal-status-modal error</xsl:attribute>
            </xsl:if>
            <div class="modal-content">
                <div class="modal-header">
                    <span class="close hal-status-close">X</span>
                    <h4>
                        <i18n:text>xmlui.hal.status</i18n:text>
                    </h4>
                </div>
                <div class="modal-body">
                    <xsl:choose>
                        <xsl:when test="$identifier">
                            <xsl:variable name="link">
                                <xsl:value-of
                                        select="concat($halLinkBase,$identifier,'v',$version)"/>
                            </xsl:variable>

                            <table class="table table-hover">
                                <tbody>
                                    <tr>
                                        <td class="table-label">
                                            <i18n:text>xmlui.hal.identifier</i18n:text>
                                        </td>
                                        <td>
                                            <xsl:value-of select="$identifier"/>
                                        </td>
                                    </tr>
                                    <xsl:if test="$version">
                                        <tr>
                                            <td class="table-label">
                                                <i18n:text>xmlui.hal.version</i18n:text>
                                            </td>
                                            <td>
                                                <xsl:value-of select="$version"/>
                                            </td>
                                        </tr>
                                    </xsl:if>
                                    <xsl:if test="$retrievedStatus">
                                        <tr>
                                            <td class="table-label">
                                                <i18n:text>xmlui.hal.status</i18n:text>
                                            </td>
                                            <td>
                                                <i18n:text>
                                                    <xsl:value-of select="$retrievedStatus"/>
                                                </i18n:text>
                                            </td>
                                        </tr>
                                    </xsl:if>
                                    <tr>
                                        <td class="table-label">
                                            <i18n:text>xmlui.hal.link</i18n:text>
                                        </td>
                                        <td>
                                            <a target="_blank">
                                                <xsl:attribute name="href">
                                                    <xsl:value-of select="$link"/>
                                                </xsl:attribute>
                                                <xsl:value-of select="$link"/>
                                            </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="table-label">
                                            <i18n:text>xmlui.hal.compliance</i18n:text>
                                        </td>
                                        <td>
                                            <i18n:text><xsl:value-of select="$halCompliance"/></i18n:text>
                                            <xsl:text> </xsl:text>
                                            <a target="_blank">
                                                <xsl:attribute name="href">
                                                    <xsl:value-of select="$halComplianceLink"/>
                                                </xsl:attribute>
                                                <i18n:text>xmlui.hal.compliance.info.link</i18n:text>
                                            </a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <xsl:if test="$retrievedStatus ='accept' and . ='halValid'">
                                <a class="btn btn-default">
                                    <xsl:attribute name="href">
                                        <xsl:value-of select="concat($context-path,'/hal/resend/',$itemID)"/>
                                    </xsl:attribute>
                                    <i18n:text>xmlui.hal.send</i18n:text>
                                </a>
                                <a class="btn btn-default">
                                    <xsl:attribute name="href">
                                        <xsl:value-of select="concat($context-path,'/hal/resend-metadata/',$itemID)"/>
                                    </xsl:attribute>
                                    <i18n:text>xmlui.hal.send.metadata</i18n:text>
                                </a>
                            </xsl:if>
                            <xsl:if test="$retrievedStatus ='update' and . ='halValid'">
                                <a class="btn btn-default">
                                    <xsl:attribute name="href">
                                        <xsl:value-of select="concat($context-path,'/hal/resend/',$itemID)"/>
                                    </xsl:attribute>
                                    <i18n:text>xmlui.hal.send.correction</i18n:text>
                                </a>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <div>
                                <p>
                                    <span class="bold">
                                        <i18n:text>xmlui.hal.compliance</i18n:text>
                                    </span>
                                    <xsl:text>: </xsl:text>
                                    <i18n:text>
                                        <xsl:value-of select="$halCompliance"/>
                                    </i18n:text>
                                    <xsl:text> </xsl:text>
                                    <a target="_blank">
                                        <xsl:attribute name="href">
                                            <xsl:value-of select="$halComplianceLink"/>
                                        </xsl:attribute>
                                        <i18n:text>xmlui.hal.compliance.info.link</i18n:text>
                                    </a>
                                </p>
                            </div>
                            <xsl:choose>
                                <xsl:when test=". ='halValid'">
                                    <p>
                                        <i18n:text>xmlui.not.in.hal</i18n:text>
                                    </p>
                                    <a class="btn btn-default">
                                        <xsl:attribute name="href">
                                            <xsl:value-of select="concat($context-path,'/hal/resend/',$itemID)"/>
                                        </xsl:attribute>
                                        <i18n:text>xmlui.hal.send.first</i18n:text>
                                    </a>
                                </xsl:when>
                                <xsl:otherwise>
                                    <p>
                                        <i18n:text>xmlui.hal.not_valid_1</i18n:text>
                                    </p>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>

                    <xsl:if test="$error">
                        <h4>
                            <i18n:text>xmlui.hal.error</i18n:text>
                        </h4>
                        <pre>
                            <xsl:value-of select="$error"/>
                        </pre>
                    </xsl:if>
                </div>
            </div>
        </div>

    </xsl:template>


    <xsl:template name="addStructureModal">
        <xsl:param name="lookupField" select="'dc_contributor_author'"/>
        <xsl:param name="editIndex" select="''"/>

        <div class="modal add-structures" role="dialog" data-edit-index="{$editIndex}">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <span class="close modal-close structures">X</span>
                        <h4>
                            <i18n:text>modal.title.addStructures</i18n:text>
                        </h4>
                    </div>
                    <div class="modal-body already-added-structures">
                        <h4>
                            <i18n:text>modal.title.previously-added-authors</i18n:text>
                            <span class="authorName"/>
                            <span id="authorFunction" class="hidden"/>
                        </h4>
                        <div id="previousStructures">
                            <xsl:if test="$editIndex!=''">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat('previousStructures_',$editIndex)"/>
                                </xsl:attribute>
                            </xsl:if>
                        </div>
                    </div>
                    <div class="modal-body additional-structures">
                        <div id="extra-structures-lookup">
                            <div>
                                <h4>
                                    <i18n:text>
                                        <xsl:value-of
                                                select="concat('xmlui.Submission.submit.input.',$lookupField, '.fieldset.additional.legend')"/>
                                    </i18n:text>
                                </h4>

                            </div>
                            <div>
                                <input id="allowInvalidCheckbox" type="checkbox" name="allowInvalid" value="allowInvalid" data-edit-index="{$editIndex}">
                                    <i18n:text>xmlui.Submission.structure.allow-invalid-checkbox</i18n:text>
                                </input>
                            </div>
                            <div>
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat('structurelookuptogglediv-',$lookupField)"/>
                                </xsl:attribute>
                                <div class="input-group">
                                    <input type="text" class="ds-text-field form-control" name="structureByName">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat('structuresName-',$lookupField)"/>
                                        </xsl:attribute>
                                    </input>

                                    <span class="input-group-btn">
                                        <input class="ds-button-field ds-add-button btn btn-default"
                                               i18n:attr="value"
                                               type="button" data-edit-index="{$editIndex}">
                                            <xsl:attribute name="id">
                                                <xsl:value-of select="concat('structureByName_lookupButton-',$lookupField)"/>
                                            </xsl:attribute>
                                            <xsl:attribute name="name">
                                                <xsl:value-of select="concat('structureByName_lookupButton-',$lookupField)"/>
                                            </xsl:attribute>
                                            <xsl:attribute name="value">
                                                <xsl:value-of
                                                        select="concat('xmlui.Submission.submit.input.',$lookupField,'.button.structureByName')"/>
                                            </xsl:attribute>
                                        </input>
                                    </span>
                                </div>

                                <div
                                        class="lookupSpinner hidden">
                                    <i class="fa fa-spinner fa-spin fa-3x fa-fw margin-bottom">&#160;</i>
                                    <xsl:attribute name="id">
                                        <xsl:value-of select="concat('structureByName_lookupSpinner-',$lookupField)"/>
                                    </xsl:attribute>
                                </div>
                                <div id="structurelookupOuterdiv">
                                    <span class="smallNoteMessage">
                                        <i18n:text>
                                            <xsl:value-of
                                                    select="concat('xmlui.Submission.submit.input.',$lookupField, '.notewhile')"/>
                                        </i18n:text>
                                    </span>
                                    <br/>
                                    <div>
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat('structureByName_div-',$lookupField)"/>
                                        </xsl:attribute>
                                        &#160;
                                    </div>
                                    <span class="hidden label label-warning">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat('structure_name_lookup_no_results-',$lookupField)"/>
                                        </xsl:attribute>
                                        <i18n:text>
                                            <xsl:value-of
                                                    select="concat('xmlui.Submission.submit.input.',$lookupField, '.nostructures.warn')"/>
                                        </i18n:text>
                                    </span>
                                    <span class="hidden label label-danger">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat('structure_name_lookup_error-',$lookupField)"/>
                                        </xsl:attribute>
                                        <i18n:text>
                                            <xsl:value-of
                                                    select="concat('xmlui.Submission.submit.input.',$lookupField, '.nostructures.error')"/>
                                        </i18n:text>
                                    </span>
                                </div>

                            </div>
                        </div>
                        <div>
                            <input
                                    class="ds-button-field ds-add-button not_displayed btn btn-default"
                                    name="addToOriginalSelect" type="button" i18n:attr="value" data-edit-index="{$editIndex}">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat('addToOriginalSelect-',$lookupField)"/>
                                </xsl:attribute>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="concat('addToOriginalSelect-',$lookupField)"/>
                                </xsl:attribute>
                                <xsl:attribute name="value">
                                    <xsl:value-of
                                            select="concat('xmlui.Submission.submit.input.',$lookupField, '.button.structuresToAuthor')"/>
                                </xsl:attribute>
                            </input>

                        </div>

                        <div id="saveAndCancelButtons">
                            <input
                                    class="ds-button-field ds-add-button not_displayed btn btn-default"
                                    name="addToOriginalUpdate" type="button" i18n:attr="value" data-edit-index="{$editIndex}">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat('addToOriginalUpdate-',$lookupField)"/>
                                </xsl:attribute>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="concat('addToOriginalUpdate-',$lookupField)"/>
                                </xsl:attribute>
                                <xsl:attribute name="value">
                                    <xsl:value-of
                                            select="concat('xmlui.Submission.submit.input.',$lookupField, '.button.structuresToAuthorUpdate')"/>
                                </xsl:attribute>
                            </input>
                            <input
                                    class="ds-button-field ds-add-button not_displayed btn btn-default"
                                    name="addToOriginalCancel" type="button" i18n:attr="value" data-edit-index="{$editIndex}">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat('addToOriginalCancel-',$lookupField)"/>
                                </xsl:attribute>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="concat('addToOriginalCancel-',$lookupField)"/>
                                </xsl:attribute>
                                <xsl:attribute name="value">
                                    <xsl:value-of
                                            select="concat('xmlui.Submission.submit.input.',$lookupField, '.button.structuresToAuthorCancel')"/>
                                </xsl:attribute>
                            </input>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template name="addFunctionModal">
        <xsl:param name="editIndex" select="''"/>

        <div class="modal update-function" role="dialog" data-edit-index="{$editIndex}">
            <div class="modal-dialog modal-md" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <span class="close modal-close functions">X</span>
                        <h4>
                            <i18n:text>modal.title.updateFunction</i18n:text>
                        </h4>
                    </div>
                    <div class="modal-body">
                        <xsl:if test="//dri:field[@n='functionSelection']">
                            <select id="modalFunctionSelection" class="ds-select-field form-control">
                                <xsl:for-each select="//dri:field[@n='functionSelection']/dri:option">
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="@returnValue"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="."/>
                                    </option>
                                </xsl:for-each>
                            </select>
                        </xsl:if>
                        <div>
                            <button id="updateAuthorFunction" class="btn btn-default" data-edit-index="{$editIndex}">
                                <i18n:text>modal.update.function</i18n:text>
                            </button>
                            <button id="cancelAuthorFunction" class="btn btn-default">
                                <i18n:text>modal.cancel.function</i18n:text>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </xsl:template>




</xsl:stylesheet>