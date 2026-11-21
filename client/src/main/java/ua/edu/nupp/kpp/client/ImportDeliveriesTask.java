package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportDeliveriesTask extends Task<Integer> {

    private static final Logger LOGGER = Logger.getLogger(ImportDeliveriesTask.class.getName());
    private static final String EXPECTED_HEADER = "client_id;courier_id;shipment_id;order_date;total_cost";

    private final Path csvPath;
    private final HttpDeliveryClient apiClient;

    public ImportDeliveriesTask(Path csvPath, HttpDeliveryClient apiClient) {
        this.csvPath = csvPath;
        this.apiClient = apiClient;
    }

    @Override
    protected Integer call() throws Exception {
        LOGGER.info("Початок фонового CSV-імпорту з файлу: " + csvPath);
        updateMessage("Читання CSV-файлу...");
        updateProgress(0, 1);

        if (csvPath == null || !Files.exists(csvPath) || !Files.isReadable(csvPath)) {
            throw new IllegalStateException("CSV-файл не знайдено або недоступний для читання: " + csvPath);
        }

        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("CSV-файл порожній.");
        }

        String header = lines.get(0).replace("\uFEFF", "").trim();
        if (!EXPECTED_HEADER.equals(header)) {
            throw new IllegalArgumentException(
                    "Невірний заголовок CSV. Очікувалося: '" + EXPECTED_HEADER + "', отримано: '" + header + "'"
            );
        }

        List<String> dataLines = lines.subList(1, lines.size()).stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();

        int total = dataLines.size();
        if (total == 0) {
            updateProgress(1, 1);
            updateMessage("Немає рядків для імпорту.");
            LOGGER.info("CSV-файл містить лише заголовок, рядків даних немає.");
            return 0;
        }

        int processed = 0;
        int imported = 0;
        int skipped = 0;

        for (int i = 0; i < total; i++) {
            if (isCancelled()) {
                LOGGER.info("CSV-імпорт скасовано користувачем після опрацювання " + processed + " із " + total + " рядків.");
                updateMessage("Імпорт скасовано. Опрацьовано: " + processed + " із " + total + ", імпортовано: " + imported);
                break;
            }

            int fileLineNumber = i + 2;
            String line = dataLines.get(i);
            String[] fields = line.split(";", -1);

            if (fields.length != 5) {
                skipped++;
                processed++;
                LOGGER.warning("Рядок " + fileLineNumber + " пропущено: очікується 5 полів, отримано " + fields.length);
                updateProgress(processed, total);
                updateMessage("Опрацьовано " + processed + " із " + total + " (успішно: " + imported + ", пропущено: " + skipped + ")");
                continue;
            }

            try {
                long clientId = Long.parseLong(fields[0].trim());
                long courierId = Long.parseLong(fields[1].trim());
                long shipmentId = Long.parseLong(fields[2].trim());
                String orderDate = fields[3].trim();
                double totalCost = Double.parseDouble(fields[4].trim());

                if (orderDate.isEmpty()) {
                    throw new IllegalArgumentException("Порожнє поле order_date");
                }

                DeliveryOrder order = new DeliveryOrder();
                order.setClientId(clientId);
                order.setCourierId(courierId);
                order.setShipmentId(shipmentId);
                order.setOrderDate(orderDate);
                order.setTotalCost(totalCost);

                apiClient.addOrder(
                        order.getClientId(),
                        order.getCourierId(),
                        order.getShipmentId(),
                        order.getOrderDate(),
                        order.getTotalCost()
                );

                imported++;
                processed++;
                LOGGER.info("Рядок " + fileLineNumber + " успішно імпортовано (clientId=" + clientId + ", shipmentId=" + shipmentId + ")");
            } catch (NumberFormatException ex) {
                skipped++;
                processed++;
                LOGGER.log(Level.WARNING, "Рядок " + fileLineNumber + " пропущено через помилку формату числа: " + line, ex);
            } catch (IllegalArgumentException ex) {
                skipped++;
                processed++;
                LOGGER.log(Level.WARNING, "Рядок " + fileLineNumber + " пропущено: " + ex.getMessage() + " (" + line + ")", ex);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Помилка HTTP/сервера під час обробки рядка " + fileLineNumber, ex);
                throw ex;
            }

            updateProgress(processed, total);
            updateMessage("Опрацьовано " + processed + " із " + total + " (успішно: " + imported + ", пропущено: " + skipped + ")");
        }

        LOGGER.info("Завершено CSV-імпорт. Опрацьовано: " + processed + ", успішно: " + imported + ", пропущено: " + skipped);
        return imported;
    }
}