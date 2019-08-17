/**
 * Project Name:withdata-v2-web
 * Package Name:cn.withdata.common.support
 * Date: 2016-03-14 17:46
 * Copyright (c) 2015,  www.miaozhen All Rights Reserved.
 */
package com.hubpd.bigscreen.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * ClassName: RoundDoulbeSerializer <br/>
 * Reason: SomeReason <br/>
 * Date: 2018-03-14 17:46
 *
 * @author cpc
 */
public class RoundDoulbeSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException, IOException {
        //write the word 'null' if there's no value available
        if (null == value) jgen.writeNull();
        else {
            final String pattern = "0.00";
            //final String pattern = "###,###,##0.00";
            final DecimalFormat myFormatter = new DecimalFormat(pattern);
            final String output = myFormatter.format(value);
            jgen.writeNumber(output);
        }
    }
}
