package com.luxeride.taxistfg.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.luxeride.taxistfg.Model.Viaje;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generarPdfViaje(Viaje viaje) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // Configuración de fuentes
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        // Cabecera con nombre del cliente y fecha
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 1});

        // Mensaje de agradecimiento y nombre del cliente
        Paragraph thankYou = new Paragraph("Gracias por el servicio, " + viaje.getCliente().getNombre(), normalFont);
        PdfPCell leftCell = new PdfPCell(thankYou);
        leftCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(leftCell);

        // Fecha de destino
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = viaje.getHoraLlegada().format(formatter);
        Paragraph date = new Paragraph(fechaFormateada, normalFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell rightCell = new PdfPCell(date);
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTable.addCell(rightCell);
        document.add(headerTable);
        document.add(Chunk.NEWLINE);

        // Total (modificado)
        PdfPTable totalTable = new PdfPTable(1);  // Solo una columna
        totalTable.setWidthPercentage(100);

        Paragraph totalLabel = new Paragraph("Total", boldFont);
        PdfPCell totalLabelCell = new PdfPCell(totalLabel);
        totalLabelCell.setBorder(Rectangle.NO_BORDER);
        totalTable.addCell(totalLabelCell);

        Paragraph totalAmount = new Paragraph(viaje.getPrecioTotal() + " €", boldFont);
        totalAmount.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell totalAmountCell = new PdfPCell(totalAmount);
        totalAmountCell.setBorder(Rectangle.NO_BORDER);
        totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.addCell(totalAmountCell);  // Solo la celda con el total
        document.add(totalTable);

        // Línea separadora
        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));
        document.add(Chunk.NEWLINE);

        // Detalles del precio
        PdfPTable priceTable = new PdfPTable(2);
        priceTable.setWidthPercentage(100);
        priceTable.setWidths(new float[]{1, 1});

        // Precio total (modificado para mostrar el total aquí)
        Paragraph basePrice = new Paragraph("Precio total", normalFont);
        PdfPCell basePriceCell = new PdfPCell(basePrice);
        basePriceCell.setBorder(Rectangle.NO_BORDER);
        priceTable.addCell(basePriceCell);

        Paragraph basePriceAmount = new Paragraph(viaje.getPrecioTotal() + " €", normalFont);
        basePriceAmount.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell basePriceAmountCell = new PdfPCell(basePriceAmount);
        basePriceAmountCell.setBorder(Rectangle.NO_BORDER);
        basePriceAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        priceTable.addCell(basePriceAmountCell);

        // Distancia
        Paragraph distance = new Paragraph("Distancia", normalFont);
        PdfPCell distanceCell = new PdfPCell(distance);
        distanceCell.setBorder(Rectangle.NO_BORDER);
        priceTable.addCell(distanceCell);

        Paragraph distanceAmount = new Paragraph(viaje.getDistanciaKm() + " km", normalFont);
        distanceAmount.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell distanceAmountCell = new PdfPCell(distanceAmount);
        distanceAmountCell.setBorder(Rectangle.NO_BORDER);
        distanceAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        priceTable.addCell(distanceAmountCell);
        document.add(priceTable);

        // Línea separadora
        document.add(new Chunk(ls));
        document.add(Chunk.NEWLINE);

        // Datos del taxista (sin espacio adicional después de los datos)
        Paragraph driverTitle = new Paragraph("Datos taxista", boldFont);
        document.add(driverTitle);

        PdfPTable driverTable = new PdfPTable(2);
        driverTable.setWidthPercentage(100);
        driverTable.setWidths(new float[]{1, 1});

        // Nombre
        Paragraph driverName = new Paragraph("Nombre", normalFont);
        PdfPCell driverNameCell = new PdfPCell(driverName);
        driverNameCell.setBorder(Rectangle.NO_BORDER);
        driverTable.addCell(driverNameCell);

        Paragraph driverNameValue = new Paragraph(viaje.getTaxista().getNombre(), normalFont);
        driverNameValue.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell driverNameValueCell = new PdfPCell(driverNameValue);
        driverNameValueCell.setBorder(Rectangle.NO_BORDER);
        driverNameValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        driverTable.addCell(driverNameValueCell);

        // Apellidos
        Paragraph driverLastName = new Paragraph("Apellidos", normalFont);
        PdfPCell driverLastNameCell = new PdfPCell(driverLastName);
        driverLastNameCell.setBorder(Rectangle.NO_BORDER);
        driverTable.addCell(driverLastNameCell);

        Paragraph driverLastNameValue = new Paragraph(viaje.getTaxista().getApellidos(), normalFont);
        driverLastNameValue.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell driverLastNameValueCell = new PdfPCell(driverLastNameValue);
        driverLastNameValueCell.setBorder(Rectangle.NO_BORDER);
        driverLastNameValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        driverTable.addCell(driverLastNameValueCell);

        // Modelo coche
        Paragraph carModel = new Paragraph("Modelo coche", normalFont);
        PdfPCell carModelCell = new PdfPCell(carModel);
        carModelCell.setBorder(Rectangle.NO_BORDER);
        driverTable.addCell(carModelCell);

        Paragraph carModelValue = new Paragraph(viaje.getCoche().getModelo(), normalFont);
        carModelValue.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell carModelValueCell = new PdfPCell(carModelValue);
        carModelValueCell.setBorder(Rectangle.NO_BORDER);
        carModelValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        driverTable.addCell(carModelValueCell);

        // Resto de los datos (marca, matrícula, licencia) con el mismo formato...

        document.add(driverTable);

        // Línea separadora
        document.add(new Chunk(ls));
        document.add(Chunk.NEWLINE);

        // Foto de la ruta
        if (viaje.getFoto() != null && viaje.getFoto().length > 0) {
            Paragraph routeTitle = new Paragraph("Ruta", boldFont);
            document.add(routeTitle);
            document.add(Chunk.NEWLINE);

            Image routeImage = Image.getInstance(viaje.getFoto());
            routeImage.scaleToFit(500, 300);
            routeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(routeImage);
        }

        document.close();
        return baos.toByteArray();
    }
}
