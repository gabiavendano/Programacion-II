package app.model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {

    public static void generarReciboPDF(Recibo recibo, Cliente cliente, Propiedad propiedad, Contrato contrato, String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Cabecera del recibo
            writer.write("RECIBO DE ALQUILER\n");
            writer.write("==================\n\n");

            // Información básica
            writer.write("Fecha de Emisión: " + recibo.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
            writer.write("Mes de Referencia: " + recibo.getMesReferencia() + "\n");
            writer.write("Fecha de Vencimiento: " + recibo.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n");

            // Información del cliente
            writer.write("CLIENTE:\n");
            writer.write("--------\n");
            writer.write("Nombre: " + cliente.getNombre() + " " + cliente.getApellido() + "\n");
            writer.write("DNI: " + cliente.getDni() + "\n");
            writer.write("Teléfono: " + cliente.getTelefono() + "\n");
            writer.write("Email: " + cliente.getEmail() + "\n\n");

            // Información de la propiedad
            writer.write("PROPIEDAD:\n");
            writer.write("----------\n");
            writer.write("Dirección: " + propiedad.getDireccion() + "\n");
            writer.write("Tipo: " + propiedad.getTipo() + "\n");
            writer.write("Tipo de Alquiler: " + contrato.getTipoAlquiler() + "\n\n");

            // Detalles del pago
            writer.write("DETALLES DEL PAGO:\n");
            writer.write("------------------\n");
            writer.write(String.format("%-20s: $ %,.2f\n", "Monto de Alquiler", recibo.getMontoAlquiler()));
            writer.write(String.format("%-20s: $ %,.2f\n", "Servicios", recibo.getServicios()));
            writer.write(String.format("%-20s: $ %,.2f\n", "TOTAL", recibo.getTotal()));

            // Línea separadora
            writer.write("\n" + "=".repeat(50) + "\n\n");

            // Texto descriptivo
            writer.write("Recibí del señor " + cliente.getNombre() + " " + cliente.getApellido() + "\n");
            writer.write("la cantidad de $" + String.format("%,.2f", recibo.getTotal()) + "\n");
            writer.write("correspondiente al alquiler de la propiedad ubicada en:\n");
            writer.write(propiedad.getDireccion() + "\n");
            writer.write("para el mes de " + recibo.getMesReferencia() + ".\n\n");

            // Firma
            writer.write("\n\n");
            writer.write("_________________________\n");
            writer.write("Firma y sello\n\n");

            // Información de la inmobiliaria
            writer.write("INMOBILIARIA DEL CASTILLO\n");
            writer.write("Tel: 3541-557179| Email: delcastilloinmobiliariavcp@gmail.com\n");
            writer.write("Dirección: Villa Carlos Paz\n");

            System.out.println("Recibo generado exitosamente: " + nombreArchivo);

        } catch (IOException e) {
            System.err.println("Error al generar el recibo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}