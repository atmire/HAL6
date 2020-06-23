package com.atmire.dspace.hal.xmlgenerating.attributes.prechecked;

import com.atmire.dspace.hal.xmlgenerating.attributes.metadata.ValueAsAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.config.MetadataValueEqualsCondition;
import org.apache.commons.collections.CollectionUtils;
import org.dspace.content.Item;
import org.w3c.dom.Element;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 09/02/2018.
 */
public class ConditionCheckingValueAsAttributeGenerator extends ValueAsAttributeGenerator implements ConditionCheckingDataAttributGenerator{

    List<MetadataValueEqualsCondition> conditionsToMatch;

    @Override
    public void addAttribute(Item item, Element element) {
        boolean conditionsMet = true;
        if (CollectionUtils.isNotEmpty(conditionsToMatch)) {
            Iterator<MetadataValueEqualsCondition> iterator = conditionsToMatch.iterator();
            while(iterator.hasNext() && conditionsMet){
                conditionsMet = iterator.next().conditionMet(item);
            }

        }
        if (conditionsMet) {
            element.setAttribute(getName(), getValue());
        }
    }


    public void setConditionsToMatch(List<MetadataValueEqualsCondition> conditionsToMatch) {
        this.conditionsToMatch = conditionsToMatch;
    }

}
