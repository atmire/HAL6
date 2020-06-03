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
                exclude-result-prefixes="i18n dri mets xlink xsl dim xhtml mods dc">

    <xsl:output indent="yes"/>

    <!-- The handling of the special case of instanced composite fields under "form" lists -->
    <xsl:template match="dri:field[@type='composite'][dri:field/dri:instance | dri:params/@operations]"
                  mode="formComposite" priority="2">
        <xsl:choose>
            <xsl:when test="@rend='submit-name hal-lookup'">
                <div>
                    <div>
                        <xsl:call-template name="renderCompositeField"/>
                    </div>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="renderCompositeField"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="renderCompositeField">

        <xsl:variable name="confidenceIndicatorID" select="concat(translate(@id,'.','_'),'_confidence_indicator')"/>
        <div class="ds-form-content">
            <div>
                <xsl:attribute name="class">
                    <xsl:text>control-group row</xsl:text>
                    <xsl:if test="dri:error">
                        <xsl:text> has-error</xsl:text>
                    </xsl:if>
                </xsl:attribute>
                <xsl:apply-templates select="dri:label" mode="compositeLabel"/>
                <xsl:apply-templates select="dri:field" mode="compositeComponent"/>


                <xsl:if test="dri:params/@choicesPresentation = 'lookup' or contains(dri:params/@operations,'add') or dri:params/@choicesPresentation = 'suggest' or dri:params/@choicesPresentation = 'authorLookup'">
                    <div class="col-xs-2">
                        <xsl:attribute name="class">
                            <xsl:choose>
                                <xsl:when test="dri:params/@choicesPresentation = 'lookup'">
                                    <xsl:text>col-xs-3 col-sm-2</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>col-xs-2</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>

                        <xsl:if test="dri:field/dri:label">
                            <label>
                                <xsl:attribute name="class">
                                    <xsl:text>control-label</xsl:text>
                                    <xsl:if test="dri:field/@required = 'yes'">
                                        <xsl:text> required</xsl:text>
                                    </xsl:if>
                                </xsl:attribute>
                                <xsl:text>&#160;</xsl:text>
                            </label>
                        </xsl:if>
                        <div class="clearfix">
                            <xsl:if test="contains(dri:params/@operations,'add')">
                                <button type="submit" name="{concat('submit_',@n,'_add')}"
                                        class="ds-button-field btn btn-default pull-right ds-add-button">
                                    <xsl:if test="dri:params/@choicesPresentation = 'lookup'">
                                        <xsl:attribute name="style">
                                            <xsl:text>display:none;</xsl:text>
                                        </xsl:attribute>
                                    </xsl:if>
                                    <!-- Make invisible if we have choice-lookup operation that provides its own Add. -->
                                    <i18n:text>xmlui.mirage2.forms.instancedCompositeFields.add</i18n:text>
                                </button>
                            </xsl:if>

                            <xsl:choose>
                                <!-- insert choice mechansim and/or Add button here -->
                                <xsl:when test="dri:params/@choicesPresentation = 'suggest'">
                                    <xsl:message terminate="yes">
                                        <i18n:text>xmlui.mirage2.forms.instancedCompositeFields.noSuggestionError
                                        </i18n:text>
                                    </xsl:message>
                                </xsl:when>
                                <!-- lookup popup includes its own Add button if necessary. -->
                                <xsl:when test="dri:params/@choicesPresentation = 'lookup'">
                                    <xsl:call-template name="addLookupButton">
                                        <xsl:with-param name="isName" select="'true'"/>
                                        <xsl:with-param name="confIndicator" select="$confidenceIndicatorID"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="dri:params/@choicesPresentation = 'authorLookup'">
                                    <xsl:call-template name="addLookupButtonAuthor">
                                        <xsl:with-param name="isName" select="'true'"/>
                                        <xsl:with-param name="confIndicator" select="$confidenceIndicatorID"/>
                                    </xsl:call-template>
                                </xsl:when>
                            </xsl:choose>
                        </div>
                    </div>
                </xsl:if>
                <!-- place to store authority value -->
                <xsl:if test="dri:params/@authorityControlled">
                    <xsl:call-template name="authorityConfidenceIcon">
                        <xsl:with-param name="confidence" select="dri:value[@type='authority']/@confidence"/>
                        <xsl:with-param name="id" select="$confidenceIndicatorID"/>
                    </xsl:call-template>
                    <xsl:call-template name="authorityInputFields">
                        <xsl:with-param name="name" select="@n"/>
                        <xsl:with-param name="authValue" select="dri:value[@type='authority']/text()"/>
                        <xsl:with-param name="confValue" select="dri:value[@type='authority']/@confidence"/>
                    </xsl:call-template>
                </xsl:if>
            </div>

            <xsl:if test="not(@rend='submit-name hal-lookup')">
                <xsl:apply-templates select="dri:help" mode="help"/>
            </xsl:if>
            <xsl:apply-templates select="dri:error" mode="compositeComponent"/>
            <xsl:apply-templates select="dri:field/dri:error" mode="compositeComponent"/>
            <xsl:if test="@rend='submit-name hal-lookup'">
                <xsl:variable name="lookupField">
                    <xsl:value-of select="substring-after(../dri:field/@id,'aspect.submission.StepTransformer.field.')"/>
                </xsl:variable>
                <xsl:call-template name="authorstructurelookup">
                    <xsl:with-param name="lookupField">
                        <xsl:value-of select="$lookupField"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>

            <xsl:if test="dri:instance or dri:field/dri:instance">
                <div class="ds-previous-values">
                    <xsl:call-template name="fieldIterator">
                        <xsl:with-param name="position">1</xsl:with-param>
                    </xsl:call-template>
                    <xsl:if test="contains(dri:params/@operations,'delete') and (dri:instance or dri:field/dri:instance)">
                        <!-- Delete buttons should be named "submit_[field]_delete" so that we can ignore errors from required fields when simply removing values-->
                        <button type="submit" name="{concat('submit_',@n,'_delete')}"
                                class="ds-button-field ds-delete-button btn btn-default">
                            <i18n:text>xmlui.mirage2.forms.instancedCompositeFields.remove</i18n:text>
                        </button>
                    </xsl:if>
                    <xsl:for-each select="dri:field">
                        <xsl:apply-templates select="dri:instance" mode="hiddenInterpreter"/>
                    </xsl:for-each>
                </div>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template match="dri:field[@type='composite']" mode="formComposite" >
        <label>
            <xsl:if test="@required='yes'">
                <xsl:attribute name="class">control-label required</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="dri:label" mode="compositeComponent"/>
            <xsl:text>:&#160;</xsl:text>
        </label>

        <div class="ds-form-content row">
            <xsl:variable name="confidenceIndicatorID" select="concat(translate(@id,'.','_'),'_confidence_indicator')"/>
            <xsl:apply-templates select="dri:field" mode="compositeComponent"/>
            <xsl:choose>
                <xsl:when test="dri:params/@choicesPresentation = 'suggest'">
                    <xsl:message terminate="yes">
                        <xsl:text>ERROR: Input field with "suggest" (autocomplete) choice behavior is not implemented for Composite (e.g. "name") fields.</xsl:text>
                    </xsl:message>
                </xsl:when>
                <!-- lookup popup includes its own Add button if necessary. -->
                <xsl:when test="dri:params/@choicesPresentation = 'lookup'">
                    <xsl:call-template name="addLookupButton">
                        <xsl:with-param name="isName" select="'true'"/>
                        <xsl:with-param name="confIndicator" select="$confidenceIndicatorID"/>
                    </xsl:call-template>
                </xsl:when>
            </xsl:choose>
            <xsl:if test="dri:params/@authorityControlled">
                <xsl:variable name="confValue" select="dri:field/dri:value[@type='authority'][1]/@confidence"/>
                <xsl:call-template name="authorityConfidenceIcon">
                    <xsl:with-param name="confidence" select="$confValue"/>
                    <xsl:with-param name="id" select="$confidenceIndicatorID"/>
                </xsl:call-template>
                <xsl:call-template name="authorityInputFields">
                    <xsl:with-param name="name" select="@n"/>
                    <xsl:with-param name="authValue" select="dri:field/dri:value[@type='authority'][1]/text()"/>
                    <xsl:with-param name="confValue" select="$confValue"/>
                </xsl:call-template>
            </xsl:if>
            <div class="spacer">&#160;</div>
            <div class="col-xs-12">
                <xsl:apply-templates select="dri:field/dri:error" mode="compositeComponent"/>
                <xsl:apply-templates select="dri:error" mode="compositeComponent"/>
                <xsl:apply-templates select="dri:help" mode="compositeComponent"/>
            </div>

            <xsl:if test="@rend='submit-name hal-lookup'">
                <xsl:variable name="lookupField">
                    <xsl:value-of select="substring-after(@id,'aspect.submission.StepTransformer.field.')"/>
                </xsl:variable>
                <xsl:call-template name="authorstructurelookup">
                    <xsl:with-param name="lookupField">
                        <xsl:value-of select="$lookupField"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </div>
    </xsl:template>



    <xsl:template name="authorstructurelookup">
        <xsl:param name="lookupField" select="'dc_contributor_author'"/>
        <div>
            <xsl:attribute name="id">
                <xsl:value-of select="concat('select-',$lookupField)"/>
            </xsl:attribute>
        </div>

        <xsl:call-template name="addStructureModal">
            <xsl:with-param name="lookupfield">
                <xsl:value-of select="$lookupField"/>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="addFunctionModal"/>
    </xsl:template>


    <xsl:template match="dri:instance" mode="interpreted">
        <xsl:choose>
            <xsl:when test="dri:value[@type='html']">
                <span class="ds-interpreted-field" ><xsl:apply-templates select="dri:value[@type='html']" mode="no-output-escaping"/></span>
            </xsl:when>
            <xsl:when test="ancestor::dri:field[contains(@rend,'hal-lookup') or @id='aspect.submission.StepTransformer.field.dc_contributor_author']">
                <span class="ds-interpreted-field" ><xsl:apply-templates select="dri:value[@type='raw']" mode="interpreted-with-breaks"/></span>
            </xsl:when>
            <xsl:when test="dri:value[@type='interpreted']">
                <span class="ds-interpreted-field"><xsl:apply-templates select="dri:value[@type='interpreted']" mode="interpreted"/></span>
            </xsl:when>
            <xsl:when test="dri:value[@type='raw']">
                <span class="ds-interpreted-field"><xsl:apply-templates select="dri:value[@type='raw']" mode="interpreted"/></span>
            </xsl:when>
            <xsl:when test="dri:value[@type='default']">
                <span class="ds-interpreted-field"><xsl:apply-templates select="dri:value[@type='default']" mode="interpreted"/></span>
            </xsl:when>
            <xsl:otherwise>
                <span class="ds-interpreted-field">No value submitted.</span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="dri:item[@n='structureValues']">
        <div class="ds-form-item row">
            <div class="control-group col-sm-12 structureValueDiv">
                <xsl:call-template name="valueWithLineBreaks"/>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="dri:value" mode="no-output-escaping">
        <xsl:value-of select="." disable-output-escaping="yes"/>
    </xsl:template>

    <xsl:template match="dri:value" mode="interpreted-with-breaks">
        <xsl:call-template name="valueWithLineBreaks"/>
    </xsl:template>

    <xsl:template name="valueWithLineBreaks">
        <xsl:param name="text" select="."/>
        <xsl:choose>
            <xsl:when test="contains($text, '&#xa;')">
                <xsl:value-of select="substring-before($text, '&#xa;')" disable-output-escaping="yes"/>
                <br/>
                <xsl:call-template name="valueWithLineBreaks">
                    <xsl:with-param name="text" select="substring-after($text,'&#xa;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!-- A set of standard attributes common to all fields -->
    <xsl:template name="fieldAttributes">
        <xsl:call-template name="standardAttributes">
            <xsl:with-param name="class">
                <xsl:text>ds-</xsl:text><xsl:value-of select="@type"/><xsl:text>-field </xsl:text>
                <xsl:choose>
                    <xsl:when test="@type='button'">
                        <xsl:text>btn</xsl:text>
                        <xsl:if test="not(contains(@rend, 'btn-'))">
                            <xsl:text> btn-default</xsl:text>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="not(@type='file')">
                        <xsl:text>form-control </xsl:text>
                    </xsl:when>
                </xsl:choose>

                <xsl:if test="@rend">
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="@rend
                    "/>
                </xsl:if>
                <xsl:if test="dri:error or parent::node()[@type='composite']/dri:error">
                    <xsl:text>input-with-feedback </xsl:text>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="placeholder">
                <xsl:if test="@placeholder">
                    <xsl:value-of select="@placeholder"/>
                </xsl:if>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:if test="@disabled='yes' or ../@rend = 'disabled'">
            <xsl:attribute name="disabled">disabled</xsl:attribute>
        </xsl:if>
        <xsl:if test="@readonly='yes' or ../@rend = 'readonly'">
            <xsl:attribute name="readonly">readonly</xsl:attribute>
        </xsl:if>
        <xsl:if test="@type != 'checkbox' and @type != 'radio' ">
            <xsl:attribute name="name"><xsl:value-of select="@n"/></xsl:attribute>
        </xsl:if>
        <xsl:if test="@type != 'select' and @type != 'textarea' and @type != 'checkbox' and @type != 'radio' ">
            <xsl:attribute name="type">
                <xsl:choose>
                    <xsl:when test="@n = 'login_email'">
                        <xsl:text>email</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@type"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>

        </xsl:if>
        <xsl:if test="@type= 'textarea'">
            <xsl:attribute name="onfocus">javascript:tFocus(this);</xsl:attribute>
        </xsl:if>
    </xsl:template>


    <!-- The iterator is a recursive function that creates a checkbox (to be used in deletion) for
      each value instance and interprets the value inside. It also creates a hidden field from the
      raw value contained in the instance.

       What makes it different from the simpleFieldIterator is that it works with a composite field's
      components rather than a single field, which requires it to consider several sets of instances. -->
    <xsl:template name="fieldIterator">
        <xsl:param name="position"/>
        <!-- add authority value for this instance -->
        <xsl:if test="dri:instance[position()=$position]/dri:value[@type='authority']">
            <xsl:call-template name="authorityInputFields">
                <xsl:with-param name="name" select="@n"/>
                <xsl:with-param name="position" select="$position"/>
                <xsl:with-param name="authValue" select="dri:instance[position()=$position]/dri:value[@type='authority']/text()"/>
                <xsl:with-param name="confValue" select="dri:instance[position()=$position]/dri:value[@type='authority']/@confidence"/>
            </xsl:call-template>
        </xsl:if>


        <xsl:choose>
            <!-- First check to see if the composite itself has a non-empty instance value in that
                position. In that case there is no need to go into the individual fields. -->
            <xsl:when test="count(dri:instance[position()=$position]/dri:value[@type != 'authority'])">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="{concat(@n,'_',$position)}" name="{concat(@n,'_selected')}"/>
                        <xsl:apply-templates select="dri:instance[position()=$position]" mode="interpreted"/>
                        <xsl:call-template name="authorityConfidenceIcon">
                            <xsl:with-param name="confidence"
                                            select="dri:instance[position()=$position]/dri:value[@type='authority']/@confidence"/>
                        </xsl:call-template>
                    </label>
                    <xsl:if test="contains(../dri:field/@rend,'hal-lookup')">
                        <div class="btn-group">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat(../dri:field/@n,'-',$position)"/>
                                </xsl:attribute>
                                <i18n:text>xmlui.hal.manage.author</i18n:text>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="" class="structuresToExistingAuthors">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat(../dri:field/@n,'-',$position)"/>
                                        </xsl:attribute>
                                        <i18n:text>xmlui.hal.manage.structures</i18n:text>
                                    </a>
                                </li>
                                <li>
                                    <a href="" class="alterFunction">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat(../dri:field/@n,'-',$position)"/>
                                        </xsl:attribute>
                                        <i18n:text>xmlui.hal.manage.functions</i18n:text>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </xsl:if>
                </div>

                <xsl:call-template name="fieldIterator">
                    <xsl:with-param name="position"><xsl:value-of select="$position + 1"/></xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <!-- Otherwise, build the string from the component fields -->
            <xsl:when test="dri:field/dri:instance[position()=$position]">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="{concat(@n,'_',$position)}" name="{concat(@n,'_selected')}"/>
                        <xsl:apply-templates select="dri:field" mode="compositeField">
                            <xsl:with-param name="position" select="$position"/>
                        </xsl:apply-templates>
                    </label>
                    <xsl:if test="contains(../dri:field/@rend,'hal-lookup')">
                        <div class="btn-group">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="concat(../dri:field/@n,'-',$position)"/>
                                </xsl:attribute>
                                <i18n:text>xmlui.hal.manage.author</i18n:text>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="" class="structuresToExistingAuthors">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat(../dri:field/@n,'-',$position)"/>
                                        </xsl:attribute>
                                        <i18n:text>xmlui.hal.manage.structures</i18n:text>
                                    </a>
                                </li>
                                <li>
                                    <a href="" class="alterFunction">
                                        <xsl:attribute name="id">
                                            <xsl:value-of select="concat(../dri:field/@n,'-',$position)"/>
                                        </xsl:attribute>
                                        <i18n:text>xmlui.hal.manage.functions</i18n:text>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </xsl:if>
                </div>

                <xsl:call-template name="fieldIterator">
                    <xsl:with-param name="position"><xsl:value-of select="$position + 1"/></xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="//dri:field[@type='button' and starts-with(@n,'edit-author-manage')]">
        <xsl:variable name="index">
            <xsl:value-of select="substring-after(@n,'edit-author-manage_')"/>
        </xsl:variable>
        <div class="btn-group">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <i18n:text>xmlui.hal.manage.author</i18n:text>
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="" class="structuresToExistingAuthors" data-edit-index="{$index}">
                        <i18n:text>xmlui.hal.manage.structures</i18n:text>
                    </a>
                </li>
                <li>
                    <a href="" class="alterFunctionEditPage" data-edit-index="{$index}">
                        <i18n:text>xmlui.hal.manage.functions</i18n:text>
                    </a>
                </li>
            </ul>
        </div>


        <xsl:call-template name="addStructureModal">
            <xsl:with-param name="lookupfield">
                <xsl:value-of select="'dc_contributor_author'"/>
            </xsl:with-param>
            <xsl:with-param name="editIndex">
                <xsl:value-of select="$index"/>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="addFunctionModal">
            <xsl:with-param name="editIndex">
                <xsl:value-of select="$index"/>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

</xsl:stylesheet>
