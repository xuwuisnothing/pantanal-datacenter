/**
 *
 */
package com.pantanal.data.task;

import java.io.File;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantanal.data.service.house.HouseService;
import com.pantanal.data.service.ProxyIpService;

/**
 * @author gudong
 *
 */
@Component("taskManager")
public class TaskManager {
    private static Logger log = LoggerFactory.getLogger(TaskManager.class);

    @Resource
    private HouseService houseService;
    @Resource
    private ProxyIpService proxyIpService;

    @Scheduled(cron = "00 00 01 * * *")
    public void importHouse() {
        log.info("=====importHouse start=====");
        String dirPath = "/opt/xuwu/crawl-data";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            log.error("=====importHouse error, dir:" + dirPath + " not existed!=====");
        } else if (!dir.isDirectory()) {
            log.error("=====importHouse error, dir:" + dirPath + " is not directory!=====");
        } else {
            File[] files = dir.listFiles();
            log.info("=====importHouse start import " + files.length + " files=====");
            long time;
            for (File file : files) {
                try {
                    time = System.currentTimeMillis();
                    log.info("=====importHouse import " + file.getName() + " start=====");
                    houseService.importFromFile(file);
                    time = System.currentTimeMillis() - time;
                    log.info("=====importHouse import " + file.getName() + " end cost:" + time + " Millis=====");
                } catch (Exception e) {
                    log.error("=====error file:" + file.getName(), e);
                }
            }
        }
        log.info("=====importHouse end=====");
    }

    @Scheduled(cron = "00 10/10 * * * *")
    public void checkProxyIp() {
        proxyIpService.checkProxyIp();
    }
}
