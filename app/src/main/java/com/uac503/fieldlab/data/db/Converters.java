package com.uac503.fieldlab.data.db;

import androidx.room.TypeConverter;
import com.uac503.fieldlab.model.Charge;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromChargeList(List<Charge> charges) {
        if (charges == null || charges.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charges.size(); i++) {
            Charge c = charges.get(i);
            sb.append(c.getX()).append(",").append(c.getY()).append(",").append(c.getCharge());
            if (i < charges.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    @TypeConverter
    public static List<Charge> toChargeList(String chargesString) {
        if (chargesString == null || chargesString.isEmpty()) return new ArrayList<>();
        List<Charge> list = new ArrayList<>();
        String[] items = chargesString.split(";");
        for (String item : items) {
            String[] parts = item.split(",");
            if (parts.length >= 3) {
                try {
                    float x = Float.parseFloat(parts[0]);
                    float y = Float.parseFloat(parts[1]);
                    float q = Float.parseFloat(parts[2]);
                    list.add(new Charge(x, y, q));
                } catch (NumberFormatException ignored) {}
            }
        }
        return list;
    }
}
