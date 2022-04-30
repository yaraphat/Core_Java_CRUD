package utils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class CommonUtils {


    public static <T> void printTableWithLinesAndMaxWidth(List<T> list, Map<String, Object> args) {
        if (list != null && !list.isEmpty()) {
            List<String[]> tableData = getTableDataFromObjectList(list);
            boolean shouldJustifyRowsToLeft = (boolean) args.getOrDefault("shouldJustifyRowsToLeft", false);
            boolean shouldWrapText = (boolean) args.getOrDefault("shouldWrapText", true);
            int maxColumnWidth = (int) args.getOrDefault("maxColumnWidth", 30);
            final Map<Integer, Integer> columnLengths;
            final StringBuilder formatString;
            final String line;

            if (shouldWrapText) {
                for (int i = 0; i < tableData.size(); i++) {
                    i = processTableRows(tableData, tableData.get(i), i, maxColumnWidth);
                }
            }

            columnLengths = getMaxColumnLengthsFromRows(tableData);
            formatString = getPrintFormatStringForColumnLengths(columnLengths, shouldJustifyRowsToLeft);
            line = prepareSeparatorLingForTable(columnLengths);

            System.out.print(line);
            System.out.printf(formatString.toString(), tableData.remove(0));
            System.out.print(line);

            tableData.forEach(row -> System.out.printf(formatString.toString(), row));
            System.out.print(line);
        }
    }

    public static int processTableRows(List<String[]> finalTableList, String[] row, int rowIndex, int maxColumnWidth) {
        // If any cell data is more than max width, then it will need extra row.
        int largestColumnLength = 0;
        do {
            String[] newRow = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                // If data is less than max width, use that as it is.
                if (row[i].length() <= maxColumnWidth) {
                    newRow[i] = "";
                } else if (row[i].length() > maxColumnWidth) {
                    newRow[i] = row[i].substring(maxColumnWidth);
                    row[i] = row[i].substring(0, maxColumnWidth);
                    largestColumnLength = newRow[i].length();
                }
            }
            if (largestColumnLength > 0) {
                finalTableList.add(++rowIndex, newRow);
                row = newRow;
                largestColumnLength -= maxColumnWidth;
            }
        } while (largestColumnLength > 0);
        return rowIndex;
    }

    public static Map<Integer, Integer> getMaxColumnLengthsFromRows(List<String[]> rows) {
        Map<Integer, Integer> columnLengths = new HashMap<>();
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                int maxLengthOfIndividualColumn = columnLengths.getOrDefault(i, 0);
                maxLengthOfIndividualColumn = Math.max(row[i].length(), maxLengthOfIndividualColumn);
                columnLengths.put(i, maxLengthOfIndividualColumn);
            }
        }
        return columnLengths;
    }

    public static StringBuilder getPrintFormatStringForColumnLengths(Map<Integer, Integer> columnLengths, boolean shouldJustifyRowsToLeft) {
        final StringBuilder formatString = new StringBuilder();
        String flag = shouldJustifyRowsToLeft ? "-" : "";
        columnLengths.forEach((key, value) -> formatString.append("| %").append(flag).append(value).append("s "));
        formatString.append("|\n");
        return formatString;
    }

    public static String prepareSeparatorLingForTable(Map<Integer, Integer> columnLengths) {
        String line = columnLengths.entrySet().stream().reduce("", (ln, b) -> {
            String templn = "+-";
            templn = templn + Stream.iterate(0, (i -> ++i)).limit(b.getValue()).reduce("", (ln1, b1) -> ln1 + "-",
                    (a1, b1) -> a1 + b1);
            templn = templn + "-";
            return ln + templn;
        }, (a, b) -> a + b);
        line = line + "+\n";
        return line;
    }

    public static <T> String[] getFieldsAsTableHeader(T object) {
        return getFieldsAsTableHeader(object.getClass().getDeclaredFields());
    }

    public static <T> String[] getFieldsAsTableHeader(Field[] fields) {
        return Arrays.stream(fields).map(CommonUtils::getSpaceSeparatedFieldName).toArray(String[]::new);
    }

    public static String getSpaceSeparatedFieldName(Field field) {
        StringBuilder sb = new StringBuilder(field.getName().replace("_", " "));
        sb.replace(0, 1, Objects.toString(sb.charAt(0)).toUpperCase());
        for (int i = 2; i < sb.length(); i++) {
            if (Character.isLowerCase(sb.charAt(i)) && Character.isUpperCase(sb.charAt(i - 1))) {
                sb.insert(i - 1, " ");
                i += 1;
            } else if (i < sb.length() - 1 && Character.isLowerCase(sb.charAt(i)) && Character.isUpperCase(sb.charAt(i + 1))) {
                sb.insert(i + 1, " ");
                i += 2;
            }
        }
        return sb.toString();
    }

    public static <T> List<String[]> getTableDataFromObjectList(List<T> list) {
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        return getTableDataFromObjectList(list, fields);
    }

    public static <T> List<String[]> getTableDataFromObjectList(List<T> list, Field[] fields) {
        List<String[]> rows = new LinkedList<>();
        rows.add(getFieldsAsTableHeader(list.get(0)));
        for (T object : list) {
            rows.add(getFieldValuesFromObject(object, fields));
        }
        return rows;
    }

    public static <T> String[] getFieldValuesFromObject(T object) {
        Field[] fields = object.getClass().getDeclaredFields();
        return getFieldValuesFromObject(object, fields);
    }

    public static <T> String[] getFieldValuesFromObject(T object, Field[] fields) {
        return Stream.iterate(0, i -> ++i).limit(fields.length).map(i -> {
            try {
                return Objects.toString(fields[i].get(object));
            } catch (Exception ignored) {
            }
            return " ";
        }).toArray(String[]::new);
    }

}
