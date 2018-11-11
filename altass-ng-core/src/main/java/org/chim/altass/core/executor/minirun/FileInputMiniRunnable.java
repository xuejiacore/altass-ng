package org.chim.altass.core.executor.minirun;

import org.chim.altass.core.exception.ExecuteException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class Name: TailMiniRunnable
 * Create Date: 11/10/18 10:10 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FileInputMiniRunnable extends MiniRunnable {

    @Override
    public void run() throws ExecuteException {
        if (dataCallback == null) {
            return;
        }
        String filePath = String.valueOf(runParamMap.get("filePath"));

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dataCallback.onDataFlush(line);
            }
        } catch (IOException e) {
            throw new ExecuteException(e);
        }
    }

}
