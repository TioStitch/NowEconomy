package de.tiostitch.economy.utils;

import de.tiostitch.economy.Main;

import java.util.Arrays;

public class NumberFormat {

    public static String dezena = Main.plugin.getConfig().getString("settings.formats.Dezena");
    public static String centena = Main.plugin.getConfig().getString("settings.formats.Centena");
    public static String milhar = Main.plugin.getConfig().getString("settings.formats.Milhar");
    public static String milhão = Main.plugin.getConfig().getString("settings.formats.Milhão");
    public static String bilhão = Main.plugin.getConfig().getString("settings.formats.Bilhão");
    public static String trilhão = Main.plugin.getConfig().getString("settings.formats.Trilhão");
    public static String quadrilhão = Main.plugin.getConfig().getString("settings.formats.Quadrilhão");
    public static String quintilhão = Main.plugin.getConfig().getString("settings.formats.Quintilhão");
    public static String sextilhão = Main.plugin.getConfig().getString("settings.formats.Sextilhão");
    public static String septilhão = Main.plugin.getConfig().getString("settings.formats.Septilhão");
    public static String oitolhão = Main.plugin.getConfig().getString("settings.formats.Oitolhão");
    public static String nonilhão = Main.plugin.getConfig().getString("settings.formats.Nonilhão");
    public static String decilhão = Main.plugin.getConfig().getString("settings.formats.Decilhão");


    public static String formatNumber(long number) {
        if (Main.plugin.getConfig().getBoolean("settings.formatter-toggle")) {
            if (number < 1000) {
                // Número menor que 1000, não precisa de formatação especial
                return String.valueOf(number);
            }

            // Determinar a escala numérica
            String[] scales = new String[]{centena, dezena, milhar, milhão, bilhão, quintilhão};
            int scaleIndex = 0;
            double scaledNumber = number;

            while (scaledNumber >= 1000 && scaleIndex < scales.length - 1) {
                scaledNumber /= 1000;
                scaleIndex++;
            }

            // Formatar o número com a escala apropriada e duas casas decimais
            String formattedNumber = String.format("%.2f", scaledNumber) + scales[scaleIndex];

            return formattedNumber;
        } else {
            return String.valueOf(number);
        }
    }

    public static long parseFormattedNumber(String formattedNumber) {
        if (Main.plugin.getConfig().getBoolean("settings.formatter-toggle")) {
            // Verificar se o número está formatado corretamente
            if (!formattedNumber.matches("^\\d+(\\.\\d+)?\\s?[a-zA-Z]+$")) {
                throw new IllegalArgumentException("Número formatado inválido");
            }

            // Extrair o valor numérico e a escala
            String[] parts = formattedNumber.split("\\s");
            double value = Double.parseDouble(parts[0]);
            String scale = parts[1];

            // Determinar o fator multiplicativo com base na escala
            String[] scales = new String[]{dezena, centena, milhar, milhão, bilhão, trilhão, quadrilhão, quintilhão, sextilhão, septilhão, oitolhão, nonilhão, decilhão};
            int scaleIndex = Arrays.asList(scales).indexOf(scale);

            if (scaleIndex == -1) {
                throw new IllegalArgumentException("Escala inválida");
            }

            double factor = Math.pow(1000, scaleIndex);

            // Calcular o valor desformatado
            long desformattedNumber = (long) (value * factor);

            return desformattedNumber;
        } else {
            throw new IllegalStateException("Formatação desativada");
        }
    }
}
