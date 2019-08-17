package com.hubpd.bigscreen.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Locale;

/**
 * ClassName: ThoustandSeparatorSerializer <br/>
 * Reason: SomeReason <br/>
 * Date: 2018-03-11 11:38
 *
 * @author cpc
 */
public class ThoustandSeparatorSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        Long realVal = 0l;

        if (aLong instanceof Long) {
            realVal = (Long) aLong;
        } else if (aLong instanceof Integer) {
            realVal = ((Integer) aLong).longValue();
        } else if (aLong instanceof Double) {
            realVal = (long) Math.ceil((Double) aLong);
        } else if (aLong instanceof Float) {
            realVal = (long) Math.ceil((Double) aLong);
        }

        String retStr = String.format(Locale.US, "%,d", realVal);
        jsonGenerator.writeObject(retStr);

    }
}
