/*
 * Project: x-framework
 * Package Name: org.ike.etl.service.core
 * Author: Xuejia
 * Date Time: 2017/2/8 14:34
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.smartui.service.impl;

import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.domain.Job;
import org.chim.altass.smartui.service.INodeOPService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: TempNodeOPServiceImpl
 * Create Date: 2017/2/8 14:34
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 临时使用的节点操作实现
 */
@Service
public class TempNodeOPServiceImpl implements INodeOPService {

    /**
     * 临时存储作业的目录
     */
    private static final String SAVE_DIR = "/data/eureka/data/jdf";

    /**
     * @param job 作业实体
     * @return
     */
    @Override
    public boolean createJob(Job job) {
        String fileName = "JOB_" + job.getNodeId() + ".jdf";

        try {
            EXmlParser.writeTo(job, SAVE_DIR + File.separator + fileName);
        } catch (XmlParserException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("作业已经存储");
        return true;
    }

    /**
     * @param jobId 需要获得的作业ID
     * @return
     */
    @Override
    public Job getJobById(String jobId) {

        String fileName = "JOB_" + jobId + ".jdf";
        Job job = null;
        try {
            job = EXmlParser.readFrom(SAVE_DIR + File.separator + fileName, Job.class);
            job.refresh();
        } catch (XmlParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return job;
    }

    /**
     * @param job 需要更新的作业实体
     * @return
     */
    @Override
    public boolean updateJob(Job job) {
        String fileName = "JOB_" + job.getNodeId() + ".jdf";

        try {
            EXmlParser.writeTo(job, SAVE_DIR + File.separator + fileName);
        } catch (XmlParserException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("作业已经存储");
        return true;
    }

    /**
     * @param jobId 需要移除的作业ID
     * @return
     */
    @Override
    public boolean removeJob(String jobId) {
        String fileName = "JOB_" + jobId + ".jdf";
        File file = new File(SAVE_DIR + File.separator + fileName);
        return file.exists() && file.delete();
    }

    @Override
    public List<Job> listJobs() {
        File jobDir = new File(SAVE_DIR);
        List<Job> jobList = new ArrayList<>();
        if (jobDir.isDirectory()) {
            // 获得作业文件夹下的所有作业数据文件
            File[] jobFiles = jobDir.listFiles();
            try {
                assert jobFiles != null;
                for (File jobFile : jobFiles) {
                    Job job = EXmlParser.readFrom(SAVE_DIR + File.separator + jobFile.getName(), Job.class);
                    job.refresh();
                    jobList.add(job);
                }
            } catch (XmlParserException | IOException e) {
                e.printStackTrace();
            }
        }
        return jobList;
    }
}
