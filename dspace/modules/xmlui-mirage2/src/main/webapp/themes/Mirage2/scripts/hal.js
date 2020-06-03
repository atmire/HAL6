(function ($) {
    $(document).ready(function () {
        replaceClickEvents();

        var structuresModal = $('.add-structures');
        if (structuresModal !== undefined) {
            structuresModal = structuresModal[0];
        }

        var functionModal = $('.update-function');
        if (functionModal !== undefined) {
            functionModal = functionModal[0];
        }

        $('a.alterFunction').click(function(event){
            event.preventDefault();
            event.stopPropagation();

            var position = 0;
            var metadataField ="dc_contributor_author";
            var id= $(this).attr('id');
            if (id !== undefined) {
                metadataField = id.slice(0, id.indexOf('-'));
                position = id.split('-').pop();
            }
            var hiddenValue = $('input[name="'+metadataField+'_function_'+position+'"').val();
            $('select#modalFunctionSelection option[value="'+hiddenValue+'"]').attr('selected', 'true')
            $('body').append(functionModal);
            var updateButton = $('button#updateAuthorFunction');
            updateButton.attr('position',position)
            updateButton.attr('metadatafield',metadataField);
            functionModal.style.display = "block";
        });
        $('a.alterFunctionEditPage').click(function(event){
            event.preventDefault();
            event.stopPropagation();
            $('body').append(functionModal);
            functionModal.style.display = "block";
            var editIndex = $(this)[0].getAttribute('data-edit-index');
            if(editIndex>0){
                if (!window.DSpace) {
                    window.DSpace = {};
                }
                window.DSpace.dataEditIndex = editIndex;
                var functionValue = $('input[name="name_'+ editIndex+'_functionValue"]');
                $(functionModal).find(('select#modalFunctionSelection option[value="'+functionValue[0].value+'"]')).attr('selected', 'true');
            }
        });

        $('button#cancelAuthorFunction').click(function(event){
            event.preventDefault();
            event.stopPropagation();

            functionModal.style.display = "none";
        })

        $('button#updateAuthorFunction').click(function(event){
            event.preventDefault();
            event.stopPropagation();

            var selectedOption = $(functionModal).find('select#modalFunctionSelection option:selected');
            var text = selectedOption.text();
            var value = selectedOption.val();
            var position = $(this).attr('position');
            if(position === undefined){
                position = window.DSpace.dataEditIndex;
                $('input[name="name_'+position+'_functionValue"]').val(value)
            }
            var metadataField= $(this).attr('metadatafield');
            $(this).removeAttr('position');
            $(this).removeAttr('metadatafield');

            $('input[name="'+metadataField+'_function_'+position+'"]').val(value);
            $('span#'+metadataField+'_functionlabel_'+position).text(text);
            functionModal.style.display = "none";

            if($('form[id$="edit-item-status"]').length){
                $('button[name="submit_update"]').click();
            }
        })

        $('input[id^="structureByAuthor_lookupButton"], input[name="retry-lookup-button"], a.structuresToExistingAuthors').click(function (event) {

            var alreadyAddedAuthors = $(this).attr('class').indexOf('structuresToExistingAuthors')>=0

            event.preventDefault();
            event.stopPropagation();

            var lookupField = 'dc_contributor_author';
            var position = 0;
            var elementId = $(this).attr('id');

            if (elementId !== undefined) {
                var elementId = $(this).attr('id');
                if (alreadyAddedAuthors) {
                    lookupField = elementId.split('-')[0];
                    position = elementId.split('-')[1];
                } else {
                    lookupField = elementId.split('-')[1];
                }
            }else{
                var thisElement = $(this)[0];
                if(thisElement !== undefined){
                    position = thisElement.getAttribute('data-edit-index');
                    if (!window.DSpace) {
                        window.DSpace = {};
                    }
                    window.DSpace.dataEditIndex = position;
                    structuresModal = $('.add-structures[data-edit-index="' + position + '"]');
                    if (structuresModal !== undefined) {
                        structuresModal = structuresModal[0];
                    }
                }

            }

            $('body').append(structuresModal);
            structuresModal.style.display = "block";

            $('#structureByAuthor_lookupSpinner-' + lookupField).removeClass('invisible');
            if(alreadyAddedAuthors){
                $('#addToOriginalUpdate-' + lookupField).addClass('alreadyAddedAuthors');
                $('#addToOriginalUpdate-' + lookupField).attr('position',position);
            }else{
                $('#addToOriginalSelect-' + lookupField).removeClass('alreadyAddedAuthors');
            }


            // div to fill with StructureID's
            var checkboxDiv = $('div#aspect_submission_StepTransformer_field_' + lookupField + '_select');

            checkboxDiv.html("&nbsp;");
            var $lastName = $('#aspect_submission_StepTransformer_field_' + lookupField + '_last');
            var $firstName = $('#aspect_submission_StepTransformer_field_' + lookupField + '_first')
            var authorFunction = ""
            if(alreadyAddedAuthors){
                $lastName= $('input[name="dc_contributor_author_last_'+position+'"]');
                $firstName = $('input[name="dc_contributor_author_first_'+position+'"]');
                authorFunction = $('input[name="dc_contributor_author_function_'+position+'"]');
                if(authorFunction.length >0){
                    $('#authorFunction').text(authorFunction.val());
                } else {
                    $('#authorFunction').text($('input[name="name_' + position + '_functionValue"]').val());
                }
            }


            var lastName = encodeURIComponent($lastName.val());
            var firstName = encodeURIComponent($firstName.val());


            $('div#structure_name_lookup_no_results_authorentered-' + lookupField).addClass('hidden');
            $lastName.parent().parent().append($('div#select-' + lookupField));
            if(lastName==="" && firstName ===""){
                $('div.structures-by-author').addClass("hidden");
            }else{
                $('div.structures-by-author').removeClass("hidden");
                var combinatedName="";
                if(lastName !== undefined && lastName!==""){
                    combinatedName = lastName;
                }
                if(firstName!== undefined && combinatedName !== "" && firstName !== ""){
                    combinatedName+=", ";
                }
                if(firstName!== undefined && firstName!==""){
                    combinatedName+=firstName;
                }
                if($('form[id$="edit-item-status"]').length){
                    combinatedName = $('textarea[name="value_'+position+'"]').text();
                }
                $('.authorName').each(function () {
                    $(this).text(combinatedName);
                })


            }


            var testing = $('.already-added-structures');
            testing.addClass('hidden');
            var $previousStructures = $('#previousStructures');
            if($previousStructures.length==0){
                $previousStructures = $('#previousStructures_'+position);
            }
            $previousStructures.empty()
            var previousStructuresDiv = $previousStructures;
            if (alreadyAddedAuthors) {
                var previousStructures = $('input[name="' + lookupField + '_structure_IDAndName' + '_' + position + '"]')[0].value.split('$$$');
                if(previousStructures !== undefined && previousStructures.length > 0){
                    var checkBoxHtml = '';
                    $.each(previousStructures, function (key, value) {
                        if (value !== '' && value.indexOf('\t')>=1) {

                            var id = value.split('\t')[0];
                            var structureName = value.split('\t')[1];
                            checkBoxHtml += '<div class="checkbox"><label>';
                            checkBoxHtml += '<input type="checkbox" checked="checked" id="' + id + '" name="' + id + '" value="' + structureName + '"/>'
                            checkBoxHtml += structureName;
                            checkBoxHtml += '</label></div>';
                        }
                    });
                    testing.removeClass('hidden');
                    previousStructuresDiv.empty().html(checkBoxHtml);
                }
            } else {

                var previousStructures = $('input[name="dc_contributor_author_structure"]');
                if (previousStructures !== undefined && previousStructures.length > 0) {

                    var checkBoxHtml = '';
                    $.each(previousStructures, function (key, value) {
                        var structureText = value.nextSibling.textContent;
                        checkBoxHtml += '<div class="checkbox"><label>';
                        checkBoxHtml += '<input type="checkbox" checked="checked" id="' + value.id + '" name="' + value.id + '" value="' + structureText + '"/>'
                        checkBoxHtml += structureText;
                        checkBoxHtml += '</label></div>';
                    });
                    testing.removeClass('hidden');
                    previousStructuresDiv.empty().html(checkBoxHtml);
                }
            }


            $('#structureByName_div-' + lookupField).find('div.checkbox').remove()
            $('#structuresName-' + lookupField).val("")
            var progressWarning = $('#progresswarning-' + lookupField);
            progressWarning.removeClass('hidden');
            $('div#hal_structures_error').addClass('hidden');
            $('div#retry-lookup').addClass('hidden');

            var loc = window.location.pathname;
            var jsonPath = loc.substr(0, loc.indexOf("/handle")) + "/JSON/hal/structure";
            if (!jsonPath.startsWith(window.DSpace.context_path)) {
                jsonPath = window.DSpace.context_path + jsonPath;
            }
            $.ajax({
                cache: false,
                type: "get",
                url: jsonPath,
                contentType: "application/json",
                data: "action=getRefStructure_byAuthor&first=" + firstName + "&last=" + lastName,
                dataType: "json",
                success: function (responseData) {
                    try {
                        $('#structureByAuthor_lookupSpinner-' + lookupField).addClass('invisible');
                        if (responseData == null || responseData.Structures == null || responseData.Structures.length == 0) {
                            //No structures found, alert the user of this
                            $('div#structure_name_lookup_no_results_authorentered-' + lookupField).removeClass('hidden');
                        } else {
                            $('div#structure_name_lookup_no_results_authorentered-' + lookupField).addClass('hidden');
                        }
                        $('#structurelookuptogglediv-' + lookupField).removeClass('.collapse');
                        progressWarning.addClass('hidden');

                        var structures = responseData.Structures;

                        // Fill the checkboxDiv
                        var checkBoxHtml = '';
                        $.each(structures, function (key, value) {
                            checkBoxHtml += '<div class="checkbox"><label>';
                            checkBoxHtml += '<input type="checkbox" checked="checked" id="structure_lookup_' + value.id + '" name="structure_lookup_' + value.id + '" value="' + value.structureName + '"/>'
                            checkBoxHtml +=  value.structureName ;
                            checkBoxHtml += '</label></div>';
                        });
                        checkboxDiv.empty().html(checkBoxHtml);

                    } catch (Exception) {
                    }
                },
                error: function (e) {
                    $('div#hal_structures_error').removeClass('hidden');
                }
            });

        });

        var closeSpan = $('span.close.modal-close.structures');
        if (closeSpan !== undefined && closeSpan[0] !== undefined) {
            closeSpan[0].onclick = function () {
                structuresModal.style.display = "none";
            }
        }
        closeSpan = $('span.close.modal-close.functions');
        if (closeSpan !== undefined && closeSpan[0] !== undefined) {
            closeSpan[0].onclick = function () {
                functionModal.style.display = "none";
            }
        }
        window.onclick = function (event) {
            if (event.target == structuresModal || event.target==functionModal) {
                structuresModal.style.display = "none";
                functionModal.style.display = "none";
            }
        }


        $('a[id^="structurelookuptogglediv"]').click(function (event) {
            // Read structureName value and do Ajax request to load matching structureids-names from Webservice
            event.preventDefault();
            event.stopPropagation();
            var lookupField = $(this).attr('id').split('-')[1];


            var form = $('#structurelookuptogglediv-' + lookupField)
            form.removeClass('collapse');
            // Load the spinner so the user knows something is going on in the background
            form.find('structureByName_lookupSpinner-' + lookupField).removeClass('invisible');

            // checkboxDiv to fill with StructureID's
            var checkboxDiv = form.find('#structureByName_div' + lookupField);

            // before the search begins: clear the checkboxDiv
            checkboxDiv.empty();

            var structureName = encodeURIComponent(form.find('structuresName-' + lookupField).val());

            var loc = window.location.pathname;
            var jsonPath = loc.indexOf("handle") > 0 ? loc.substr(0, loc.indexOf("/handle")) + "/JSON/hal/structure" : loc.substr(0, loc.indexOf("/admin")) + "/JSON/hal/structure";

            if (!jsonPath.startsWith(window.DSpace.context_path)) {
                jsonPath = window.DSpace.context_path + jsonPath;
            }
            var isInvalidAllowed = $('#allowInvalidCheckbox').is(":checked");
            // Create get action to SoapServlet.java - Returns a json array with the structures (structures {{id, structureName}, {id, structureName}})
            $.ajax({
                cache: false,
                type: "get",
                url: jsonPath,
                contentType: "application/json",
                data: "action=getRefStructure_byName&name=" + structureName+ "&invalidallowed=" + isInvalidAllowed,
                dataType: "json",
                success: function (responseData) {a
                    try {
                        hideErrorFields(form);

                        form.find('#structureByName_lookupSpinner-' + lookupField).addClass('invisible');

                        if (responseData == null || responseData.Structures == null || responseData.Structures.length == 0) {
                            form.find('div#structure_name_lookup_no_results-' + lookupField).removeClass('hidden');
                        } else {
                            form.find('div#structure_name_lookup_no_results-' + lookupField).addClass('hidden');
                        }

                        var structures = responseData.Structures;
                        // Fill the checkboxDiv
                        var checkBoxHtml = '';
                        $.each(structures, function (key, value) {
                            checkBoxHtml += '<div class="checkbox"><label>';
                            checkBoxHtml += '<input type="checkbox" id="structure_lookup_' + value.id + '" name="structure_lookup_' + value.id + '" value="' + value.structureName + '"/>'
                            checkBoxHtml +=  value.structureName;
                            checkBoxHtml += '</label></div>';
                        });
                        checkboxDiv.replaceWith('<div id="structureByName_div-' + lookupField + '">' + checkBoxHtml + '</div>');

                        // This button adds the checked items to the main selectlist and has no use when no results were returned
                        if (0 < structures.length) {
                            form.find('#addToOriginalSelect-' + lookupField).show();
                        } else {
                            form.find('div#structure_name_lookup_error').removeClass('hidden');
                        }
                    } catch (Exception) {
                    }
                },
                error: function (e) {
                    hideErrorFields(form);

                    if (e.status == 500) {
                        form.find('div#structure_name_lookup_danger').removeClass('hidden');
                    }
                    else {
                        form.find('div#structure_name_lookup_error').removeClass('hidden');
                    }

                    form.find('#structureByName_lookupSpinner-' + lookupField).addClass('invisible');
                }
            });
        });


        // Add the checked items from the structure name lookup to the main selectbox/checkbox list
        $('[id^="addToOriginalSelect"]').click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            var lookupField = $(this).attr('id').split('-')[1];
            var previousStructures = $('#previousStructures');
            if(previousStructures.length==0){
                var position = $(this).closest('.modal.add-structures')[0].getAttribute('data-edit-index');
                previousStructures = $('#previousStructures_'+position);
            }
            var byAuthorStructures = $('div#aspect_submission_StepTransformer_field_' + lookupField + '_select');
            var checkboxDiv = $('div#structureByName_div-' + lookupField);

            var structures1 =previousStructures.find('input[type="checkbox"]:checked:not([disabled])');
            var structures2 = checkboxDiv.find('input[type="checkbox"]:checked:not([disabled])');
            var structures3 = byAuthorStructures.find('input[type="checkbox"]:checked:not([disabled])');
            checkboxDiv.empty();

            previousStructures.empty()
            var previousStructuresDiv = previousStructures;
            var addStructuresToCheckboxHtml = function (structures) {
                $.each(structures, function (key, value) {
                    if (value !== '' && value != undefined) {

                        var id = value.id;
                        var structureName = value.value;
                        if (id !== undefined && structureName != undefined) {
                            checkBoxHtml += '<div class="checkbox"><label>';
                            checkBoxHtml += '<input type="checkbox" checked="checked" id="' + id + '" name="' + id + '" value="' + structureName + '"/>'
                            checkBoxHtml += structureName;
                            checkBoxHtml += '</label></div>';
                        }
                    }
                });
            };
            var checkBoxHtml = '';
            addStructuresToCheckboxHtml(structures1);
            addStructuresToCheckboxHtml(structures2);
            addStructuresToCheckboxHtml(structures3);

            previousStructuresDiv.empty().html(checkBoxHtml);


            $('#structuresName-' + lookupField).val("");

        });

        var pushValuesToArray= function (structures, structureInfo){
            $.each(structures, function (value, input) {
                if(this.type!=undefined){
                    structures.push(value);
                }
            });
            return structures;
        }
        // Add the checked items from the structure name lookup to the main selectbox/checkbox list
        $('[id^="addToOriginalUpdate"]').click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            var lookupField = $(this).attr('id').split('-')[1];
            var previousStructures = $('#previousStructures');
            if(previousStructures.length==0){
                var position = $(this).closest('.modal.add-structures')[0].getAttribute('data-edit-index');
                previousStructures = $('#previousStructures_'+position);
            }
            var addedStructures = {};
            var byAuthorStructures = $('div#aspect_submission_StepTransformer_field_' + lookupField + '_select');
            var checkboxDiv = $('div#structureByName_div-' + lookupField);

            var structures1 =previousStructures.find('input[type="checkbox"]:checked:not([disabled])');
            var structures2 = checkboxDiv.find('input[type="checkbox"]:checked:not([disabled])');
            var structures3 = byAuthorStructures.find('input[type="checkbox"]:checked:not([disabled])');

            var addToPreExistingAuthor = $(this).hasClass('alreadyAddedAuthors') || $(this).closest('.modal.add-structures')[0].getAttribute('data-edit-index')>0;
            if(addToPreExistingAuthor){
                var position = $(this).attr('position');
                if(position === undefined){
                    position = window.DSpace.dataEditIndex;
                }
                var hiddenStructureInput = $('input[name="'+lookupField+'_structure_'+position+'"]');
                var editStructureInput = $('input[name="name_'+position+'_structureValues"]');
                var shownValue = $('input[value="'+lookupField+'_'+position+'"]');
                var authorName = $('span.authorName')[0].textContent;
                var authorFunction = $('span#authorFunction')[0].textContent;
                authorFunction = $('select[name="functionSelection"] option[value="'+authorFunction+'"]').text()
                var functionLabelId = lookupField+'_functionlabel_'+position;
                var structureInfo = authorName+' <span id="'+functionLabelId+'" class="label label-default">'+authorFunction+'</span>'+'\n';
                structureInfo = addStructuresToArray(structures1,structureInfo, addedStructures);
                structureInfo = addStructuresToArray(structures2,structureInfo, addedStructures);
                structureInfo = addStructuresToArray(structures3,structureInfo, addedStructures);
                var concatenatedStructureIds = '';
                var backendValue = '';

                $.each(addedStructures, function(value, input){
                    if(concatenatedStructureIds!== ''){
                        concatenatedStructureIds+='$$$';
                    }
                    concatenatedStructureIds+= value;
                    if(backendValue!== ''){
                        backendValue+='$$$';
                    }
                    backendValue+= input;
                });
                hiddenStructureInput.val(concatenatedStructureIds);
                editStructureInput.val(concatenatedStructureIds);
                if(shownValue!== undefined && shownValue[0]!== undefined){
                    $(shownValue[0].nextSibling).html(structureInfo);
                }

                $('input[name="'+lookupField+'_structure_IDAndName_'+position+'"]').val(backendValue)
            }
            checkboxDiv.empty();

            structuresModal.style.display = "none";
            if($('form[id$="edit-item-status"]').length){
                $('button[name="submit_update"]').click();
            }
        });
        $('[id^="addToOriginalCancel"]').click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            structuresModal.style.display = "none";
        });
        var addStructuresToArray= function (structures, structureInfo, addedStructures){
            $.each(structures, function (value, input) {
                var structureIdentifier = $(input).attr('name').replace('structure_lookup_', '');
                if(addedStructures[structureIdentifier] !== undefined){
                }else{
                    addedStructures[structureIdentifier] = structureIdentifier+'\t'+input.value;
                    structureInfo+= input.value+'\n';
                }
            });
            return structureInfo;
        }
        // Looking up author's structures causes the selectbox to be emptied, so once done, the enable-button has to be clicked before a new lookup can be done (prevent unwanted effects)
        $('[id^="structureByAuthor_enableAuthor"]').click(function () {
            var lookupField = $(this).attr('id').split('-')[1];

            $('#aspect_submission_StepTransformer_field_' + lookupField + '_last').attr('readonly', false);
            $('#aspect_submission_StepTransformer_field_' + lookupField + '_first').attr('readonly', false);
            $('#structureByAuthor_lookupButton-' + lookupField).attr('disabled', false);
            $(this).attr('disabled', true);
        });

        /* EDIT ITEM METADATA LOOKUP CODE */
        // On the edit item/metadata page, add data-attributes to all rows to pass on (unique authID + structureid)
        $('.metadata-value-structureids').each(function () {
            var authId = getAuthorityString($(this));
            var structureID = getstructureIdString($(this));
            $(this).attr("data-structureId", structureID);
            $(this).attr("data-authId", authId);
        });

        $('.structureId_add').click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            // RESET STATE
            try {
                $(prev).siblings().hide();
            } catch (Exception) {
                //console.log(prev);
            }
            prev = $(this);
            // Fixes toggle bug (where after 5 times clicking different Add structure buttons, the toggle happened 5 times

            // Tablerow where the new structure-row has to be inserted _before_
            var buttontr = $(this).parent().parent();

            var scope = $(this).next();

            $(this).next().fadeIn('fast');


        });
    });


    $('input[id^="structureByName_lookupButton"]').on('click', function (event) {
        event.preventDefault();
        event.stopPropagation();

        var position = $(this).attr('data-edit-index');

        var lookupField = $(this).attr('id').split('-')[1];
        var parentModal = $($(this).closest('.modal.add-structures')[0]);
        parentModal.find('#structureByName_lookupSpinner-' + lookupField).addClass('loading');

        // var checkboxDiv = $('#structureByName_div-' + lookupField);
        var checkboxDiv = parentModal.find('#structureByName_div-' + lookupField);
        // checkboxDiv to fill with structureID's
        checkboxDiv.empty();
        // before the search begins: clear the checkboxDiv

        var structureName = encodeURIComponent(parentModal.find('#structuresName-' + lookupField).val());
        var loc = window.location.pathname;
        var jsonPath = loc.substr(0, loc.indexOf("/admin")) + "/JSON/hal/structure";
        if (!jsonPath.startsWith(window.DSpace.context_path)) {
            jsonPath = window.DSpace.context_path + jsonPath;
        }
        var isInvalidAllowed = $('#allowInvalidCheckbox[data-edit-index="' + position + '"]').is(":checked");
        // Create get action to SoapServlet.java - Returns a json array with the structures (structures {{id, structureName}, {id, structureName}})
        $.ajax({
            cache: false,
            type: "get",
            url: jsonPath,
            contentType: "application/json",
            data: "action=getRefStructure_byName&name=" + structureName + "&invalidallowed=" + isInvalidAllowed,
            dataType: "json",
            success: function (responseData) {
                try {
                    hideErrorFields(lookupField);

                    $('#structureByName_lookupSpinner-' + lookupField).addClass('invisible');

                    var structures = responseData.Structures;
                    // Fill the checkboxDiv
                    var checkBoxHtml = '';
                    $.each(structures, function (key, value) {
                        checkBoxHtml += '<div class="checkbox"><label>';
                        var isDisabled = "";
                        if($('#previousStructures input[id="structure_lookup_'+value.id+'"], #previousStructures input[id="'+value.id+'"],.halByAuthorNamePrevious input[id="structure_lookup_'+value.id+'"], .halByAuthorNamePrevious input[id="'+value.id+'"]').length >0){
                            isDisabled = 'disabled="disabled" checked="checked"';
                        }
                        checkBoxHtml += '<input type="checkbox" '+isDisabled+' id="structure_lookup_' + value.id + '" name="structure_lookup_' + value.id + '" value="'+value.structureName + '"/>'
                        checkBoxHtml +=  value.structureName ;
                        checkBoxHtml += '</label></div>';
                    });
                    checkboxDiv.replaceWith('<div id="structureByName_div-' + lookupField + '">' + checkBoxHtml + '</div>')
                    if (0 < structures.length) {
                        parentModal.find('#addToOriginalSelect').show();
                    }
                    else {
                        parentModal.find('div#structure_name_lookup_error').removeClass('hidden');
                    }
                } catch (Exception) {
                }

            },
            error: function (e) {
                hideErrorFields(lookupField);

                if (e.status == 500) {
                    $('div#structure_name_lookup_danger-' + lookupField).removeClass('hidden');
                }
                else {
                    $('div#structure_name_lookup_error-' + lookupField).removeClass('hidden');
                }

                $('#structureByName_lookupSpinner-' + lookupField).addClass('invisible');
            }
        });
    });

    // Catch and replace button click to remove a row (edit item metadata - delete a structure from an author)
    function replaceClickEvents() {
        $('.structureId_remove').click(function (event) {
            // this = the input field, this.parent = the cell, this.parent.parent = row
            event.preventDefault();
            $(this).parent().parent().remove();
        });
    }

    // Help function to extract the authority id from a string
    function getAuthorityString(elem) {
        var fullId = $(elem).attr('id');
        var index = fullId.indexOf("structureIds_");
        var authId = fullId.substr(index + 7);
        index = authId.indexOf("_");
        var structureId = authId.substr(index + 1);
        authId = authId.substr(0, index);
        return authId;
    }

    // Help function to extract the structure id from a string
    function getstructureIdString(elem) {
        var fullId = $(elem).attr('id');
        var index = fullId.indexOf("structureIds_");
        var authId = fullId.substr(index + 7);
        index = authId.indexOf("_");
        var structurebId = authId.substr(index + 1);
        return structurebId;
    }

    function hideErrorFields(field) {
        $('div#structure_name_lookup_error-' + field).addClass('hidden');
        $('div#structure_name_lookup_danger-' + field).addClass('hidden');
    }


})(jQuery);


(function ($) {
    // Status-modal specific javascript
    var modal = $('.hal-status-modal')[0];
    if (modal !== undefined) {
        $('a.hal-status').click(function (event) {

            event.preventDefault();
            $('body').append(modal);
            modal.style.display = "block";
        });

        $('span.hal-status-close')[0].onclick = function () {
            modal.style.display = "none";
        }

        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    }
})(jQuery);
