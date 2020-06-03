/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */


window.DSpace.authority_fields = ['hal_journal_title', 'hal_domain', 'hal_funder_europe', 'hal_funder_anr', 'dc_contributor_author'];

window.DSpace.authority_fields['hal_journal_title']={addButtonTextValue:"Add this journal", titleValue:"Journal look up", nameValue:"Title", noSelectionValue:"Please select a journal or series title on the left.", notFoundValue:"No journal found", peopleValue:"Journals", searchTextValue:"Search for a journal or series title:" };
window.DSpace.authority_fields['hal_domain']={addButtonTextValue:"Add this domain", titleValue:"Domain look up", nameValue:"Title", noSelectionValue:"Please select a domain on the left.", notFoundValue:"No domain found", peopleValue:"domains", searchTextValue:"Search for a domain" };
window.DSpace.authority_fields['hal_funder_europe']={addButtonTextValue:"Add this european project", titleValue:"European project look up", nameValue:"Title", noSelectionValue:"Please select a european project on the left.", notFoundValue:"No european projects found", peopleValue:"european projects", searchTextValue:"Search for a european project" };
window.DSpace.authority_fields['hal_funder_anr']={addButtonTextValue:"Add this anr project", titleValue:"Anr project look up", nameValue:"Title", noSelectionValue:"Please select an anr project on the left.", notFoundValue:"No anr projects found", peopleValue:"anr projects", searchTextValue:"Search for an anr project" };
window.DSpace.authority_fields['dc_contributor_author']={addButtonTextValue:"Add this author", titleValue:"Author look up", nameValue:"Name", noSelectionValue:"Please select an author on the left.", notFoundValue:"No author found", peopleValue:"authors", searchTextValue:"Search for an author" };


function AuthorLookup(url, authorityInput, collectionID) {
    var itemsInRepo = '<label>Items in this repository:&nbsp;</label>';

    var authorityType;
    for (i = 0; i < window.DSpace.authority_fields.length; i++) {
        if(url.indexOf(window.DSpace.authority_fields[i])!=-1){
            authorityType=window.DSpace.authority_fields[i];
        }
    }

    var addButtonText = 'Add this person';
    var titleText = 'Person look up';
    var nameText = 'Name';
    var noselectionText = 'Please select an author/creator on the left.';
    var notFoundText = 'No people found';
    var peopleText = 'people';
    var searchText = 'Search for an author/creator:';

    if(authorityType !=undefined){

        addButtonText = window.DSpace.authority_fields[authorityType].addButtonTextValue;
        titleText = window.DSpace.authority_fields[authorityType].titleValue;
        nameText = window.DSpace.authority_fields[authorityType].nameValue;
        noselectionText = window.DSpace.authority_fields[authorityType].noSelectionValue;
        notFoundText = window.DSpace.authority_fields[authorityType].notFoundValue;
        peopleText = window.DSpace.authority_fields[authorityType].peopleValue;
        searchText = window.DSpace.authority_fields[authorityType].searchTextValue;
    }


//    TODO i18n
    $(".authorlookup").remove();
    var content = $(
        '<div class="authorlookup modal fade" tabindex="-1" role="dialog" aria-labelledby="personLookupLabel" aria-hidden="true">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' +
        '<h4 class="modal-title" id="personLookupLabel">' + titleText + '</h4>' +
        '</div>' +
        '<div class="modal-body">' +
        '<div title="' + titleText + '">' +
        '<table class="dttable col-xs-4">' +
        '<thead>' +
        '<th>' + nameText + '</th>' +
        '</thead>' +
        '<tbody class="break-all">' +
        '<tr><td>Loading...<td></tr>' +
        '</tbody>' +
        '</table>' +
        '<span class="no-vcard-selected">' + noselectionText + '</span>' +
        '<ul class="vcard list-unstyled" style="display: none;">' +
        '<li><ul class="variable"/></li>' +
        '<li class="vcard-insolr">' +
        itemsInRepo +
        '<span/>' +
        '</li>' +
        '<li class="vcard-add">' +
        '<input class="ds-button-field btn btn-default" value="' + addButtonText + '" type="button"/>' +
        '</li>' +
        '</ul>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>'
    );

    var moreButton = '<button id="lookup-more-button" class="btn btn-default">show more</button>';
    var lessButton = '<button id="lookup-less-button" class="btn btn-default">show less</button>';
    var button = moreButton;

    var datatable = content.find("table.dttable");
    datatable.dataTable({
        "aoColumns": [
            {
                "bSortable": false,
                "sWidth": "200px"
            },
            {
                "bSortable": false,
                "bSearchable": false,
                "bVisible": false
            }
        ],
        "oLanguage": {
            "sInfo": 'Showing _START_ to _END_ of _TOTAL_ ' + peopleText,
            "sInfoEmpty": 'Showing 0 to 0 of 0 ' + peopleText,
            "sInfoFiltered": '(filtered from _MAX_ total ' + peopleText + ')',
            "sLengthMenu": '_MENU_ people/page',
            "sSearch":'<p class="searchTextLeftFloat">'+searchText+'</p>',
            "sZeroRecords": notFoundText
        },
        "bAutoWidth": false,
        "bJQueryUI": true,
        "bProcessing": true,
        "bSort": false,
        "bPaginate": false,
        "sPaginationType": "two_button",
        "bServerSide": true,
        "sAjaxSource": url,
        "sDom": '<"H"lfr><"clearfix"t<"vcard-wrapper col-xs-8">><"F"ip>',
        "fnInitComplete": function() {
            content.find("table.dttable").show();
            content.find("div.vcard-wrapper").append(content.find('.no-vcard-selected')).append(content.find('ul.vcard'));
            content.modal();

            content.find('.dataTables_wrapper')
                .parent()
                .attr('style', 'width: auto; min-height: 121px; height: auto;');
            var searchFilter = content.find('.dataTables_filter input');
            var initialInput = "";
            if (authorityInput.indexOf('value_') != -1) { // edit item
                initialInput = $('textarea[name=' + authorityInput + ']').val();
            } else {   // submission
                var lastName = $('input[name=' + authorityInput + '_last]');
                if (lastName.size()) { // author input type
                    initialInput = (lastName.val() + " " + $('input[name=' + authorityInput + '_first]').val()).trim();
                } else { // other input types
                    initialInput = $('input[name=' + authorityInput + ']').val();
                }
            }
            searchFilter.val(initialInput);
            setTimeout(function () {
                searchFilter.trigger($.Event("keyup", { keyCode: 13 }));
            },50);
            searchFilter.trigger($.Event("keyup", {keyCode: 13}));
            searchFilter.addClass('form-control');
            content.find('.ui-corner-tr').removeClass('.ui-corner-tr');
            content.find('.ui-corner-tl').removeClass('.ui-corner-tl');

        },
        "fnInfoCallback": function( oSettings, iStart, iEnd, iMax, iTotal, sPre ) {
            return "Showing "+ iEnd + " results. "+button;
        },
        "fnRowCallback": function( nRow, aData, iDisplayIndex ) {
            aData = aData[1];
            var $row = $(nRow);

            var authorityID = $(this).closest('.dataTables_wrapper').find('.vcard-wrapper .vcard').data('authorityID');
            if (authorityID != undefined && aData['authority'] == authorityID) {
                $row.addClass('current-item');
            }

            $row.addClass('clickable');
            if(aData['insolr']=="false"){
                $row.addClass("notinsolr");
            }

            $row.click(function() {
                var $this = $(this);
                $this.siblings('.current-item').removeClass('current-item');
                $this.addClass('current-item');
                var wrapper = $this.closest('.dataTables_wrapper').find('.vcard-wrapper');
                wrapper.find('.no-vcard-selected:visible').hide();
                var vcard = wrapper.find('.vcard');
                vcard.data('authorityID', aData['authority']);
                vcard.data('name', aData['value']);

                var notDisplayed = ['insolr','value','authority'];
                var predefinedOrder = ['title', 'last-name','first-name'];
                var variable = vcard.find('.variable');
                variable.empty();
                predefinedOrder.forEach(function (entry) {
                    variableItem(aData, entry, variable);
                });

                for (var key in aData) {
                    if (predefinedOrder.indexOf(key) < 0) {
                        variableItem(aData, key, variable);
                    }
                }

                function variableItem(aData, key, variable) {
                    if (aData.hasOwnProperty(key) && notDisplayed.indexOf(key) < 0) {
                        var label = key.replace(/-/g, ' ');
                        var dataString = '';
                        dataString += '<li class="vcard-' + key + '">' +
                            '<label>' + label + ': </label>';

                        if(key == 'orcid'){
                            var orcidURL =window.DSpace.orcidConnectorURL;
                            dataString += '<span><a target="_blank" href="'+orcidURL+ aData[key] + '">' + aData[key] + '</a></span>';
                        } else {
                            dataString += '<span>' + aData[key] + '</span>';
                        }
                        dataString += '</li>';

                        variable.append(dataString);
                        return label;
                    }
                }

                if(aData['insolr']!="false"){
                    var discoverLink = window.DSpace.context_path + "/discover?filtertype=author&filter_relational_operator=authority&filter=" + aData['insolr'];
                    vcard.find('.vcard-insolr span')
                        .empty()
                        .append('<a href="' + discoverLink + '" target="_new">view items</a>');

                }else{
                    vcard.find('.vcard-insolr span').text("0");
                }

                vcard.find('.vcard-add input').click(function() {
                    if (authorityInput.indexOf('value_') != -1) {
                        // edit item
                        $('input[name=' + authorityInput + ']').val(vcard.find('.vcard-last-name span').text() + ', ' + vcard.find('.vcard-first-name span').text());
                        var oldAuthority = $('input[name=' + authorityInput + '_authority]');
                        oldAuthority.val(vcard.data('authorityID'));
                        $('textarea[name='+ authorityInput+']').val(vcard.data('name'));

                    } else {
                        // submission
                        var lastName = $('input[name=' + authorityInput + '_last]');
                        if (lastName.size()) { // author input type
                            lastName.val(vcard.find('.vcard-last-name span').text());
                            $('input[name=' + authorityInput + '_first]').val(vcard.find('.vcard-first-name span').text());
                        }
                        else { // other input types
                            $('input[name=' + authorityInput + ']').val(vcard.data('name'));
                        }

                        $('input[name=' + authorityInput + '_authority]').val(vcard.data('authorityID'));
                        $('input[name=submit_'+ authorityInput +'_add]').click();

                    }
                    content.modal('hide');
                });
                vcard.show();
            });

            return nRow;
        },
        "fnDrawCallback": function() {
            var wrapper = $(this).closest('.dataTables_wrapper');
            if (wrapper.find('.current-item').length > 0) {
                wrapper.find('.vcard-wrapper .no-vcard-selected:visible').hide();
                wrapper.find('.vcard-wrapper .vcard:hidden').show();
            }
            else {
                wrapper.find('.vcard-wrapper .vcard:visible').hide();
                wrapper.find('.vcard-wrapper .no-vcard-selected:hidden').show();
            }
            $('#lookup-more-button').click(function () {
                button = lessButton;
                datatable.fnFilter($('.dataTables_filter > input').val());
            });
            $('#lookup-less-button').click(function () {
                button = moreButton;
                datatable.fnFilter($('.dataTables_filter > input').val());
            });
        },
        "fnServerData": function (sSource, aoData, fnCallback) {
            var sEcho;
            var query;
            var start;
            var limit;

            $.each(aoData, function() {
                if (this.name == "sEcho") {
                    sEcho = this.value;
                }
                else if (this.name == "sSearch") {
                    query = this.value;
                }
                else if (this.name == "iDisplayStart") {
                    start = this.value;
                }
                else if (this.name == "iDisplayLength") {
                    limit = this.value;
                }
            });

            if (collectionID == undefined) {
                collectionID = '-1';
            }

            if (sEcho == undefined) {
                sEcho = '';
            }

            if (query == undefined) {
                query = '';
            }

            if (start == undefined) {
                start = '0';
            }

            if (limit == undefined) {
                limit = '0';
            }

            if (button == lessButton) {
                limit = '20';
            }
            if (button == moreButton) {
                limit = '10';
            }


            var data = [];
            data.push({"name": "query", "value": query});
            data.push({"name": "collection", "value": collectionID});
            data.push({"name": "start", "value": start});
            data.push({"name": "limit", "value": limit});

            var $this = $(this);

            $.ajax({
                cache: false,
                url: sSource,
                dataType: 'xml',
                data: data,
                success: function (data) {
                    /* Translate AC XML to DT JSON */
                    var $xml = $(data);
                    var aaData = [];
                    $.each($xml.find('Choice'), function() {
                        // comes from org.dspace.content.authority.SolrAuthority.java
                        var choice = this;

                        var row = [];
                        var rowData = {};

                        for(var k = 0; k < choice.attributes.length; k++) {
                            var attr = choice.attributes[k];
                            rowData[attr.name] = attr.value;
                        }

                        row.push(rowData.value);
                        row.push(rowData);
                        aaData.push(row);

                    });

                    var nbFiltered = $xml.find('Choices').attr('total');

                    var total = $this.data('totalNbPeople');
                    if (total == undefined || (total * 1) < 1) {
                        total = nbFiltered;
                        $this.data('totalNbPeople', total);
                    }

                    var json = {
                        "sEcho": sEcho,
                        "iTotalRecords": total,
                        "iTotalDisplayRecords": nbFiltered,
                        "aaData": aaData
                    };
                    fnCallback(json);
                }
            });
        }
    });
}
