/*
 * Project: x-framework
 * Package Name: org.ike.controller.support.node
 * Author: Xuejia
 * Date Time: 2017/2/24 14:49
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.smartui.controller.node;

import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.domain.Job;
import org.chim.altass.smartui.common.BaseController;
import org.chim.altass.smartui.common.CommonResult;
import org.chim.altass.smartui.service.INodeOPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: JobServiceController
 * Create Date: 2017/2/24 14:49
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Controller
@RequestMapping("jobService")
public class JobServiceController extends BaseController {

    private static final String SAVE_DIR = "/data/eureka/data/jdf";

    @Autowired
    private INodeOPService jobOpService = null;

    /**
     * 列出作业列表
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, name = "listJobs")
    public CommonResult listJobs() {
        List<Job> jobs = jobOpService.listJobs();
        Map<String, Object> jobMap;
        List<Map<String, Object>> result = new ArrayList<>();
        for (Job job : jobs) {
            jobMap = new HashMap<>();
            jobMap.put("jobId", job.getNodeId());
            jobMap.put("jobName", job.getNodeName());
            jobMap.put("entriesSize", job.getEntries().getEntries().size());
            result.add(jobMap);
        }

        CommonResult commonResult = new CommonResult(true);
        commonResult.setResult(result);
        commonResult.setCode(HttpStatus.OK.value());
        return commonResult;
    }

    /**
     * 将作业数据文件上传并解析
     *
     * @param request  -
     * @param response -
     * @return 返回解析后的job数据
     */
    @RequestMapping(value = "/uploadJdf")
    @ResponseBody
    public CommonResult jobDataFileUpload(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
        CommonResult result = new CommonResult(false);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        MultipartFile partFile = fileMap.get("jdfElem");
        if (partFile == null) {
            return result;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(partFile.getInputStream()));

        StringBuilder jdfContent = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            jdfContent.append(line);
        }
        Job job;
        try {
            if (jdfContent.length() > 0) {
                job = EXmlParser.fromXml(jdfContent.toString(), Job.class);
                result.setResult(job);
                String fileName = "JOB_" + job.getNodeId() + ".jdf";

                try {
                    EXmlParser.writeTo(job, SAVE_DIR + File.separator + fileName);
                } catch (XmlParserException e) {
                    e.printStackTrace();
                }
                result.setFlag(true);
                System.out.println("作业已经转储");
            } else {
                result.setFlag(false);
                return result;
            }
        } catch (XmlParserException e) {
            e.printStackTrace();
            result.setFlag(false);
        }
        return result;
    }

    //    @ResponseBody
    @RequestMapping(value = "/saveJdf")
    public void downloadJobDataFile(HttpServletResponse response, @RequestParam(value = "jobId", required = true) String jobId) throws IOException {

        CommonResult commonResult = new CommonResult(false);
        String path = SAVE_DIR + File.separator + "JOB_" + jobId + ".jdf";

        File file = new File(path);// path是根据日志路径和文件名拼接出来的
        String filename = file.getName();// 获取日志文件名称
        InputStream fis = new BufferedInputStream(new FileInputStream(path));
        byte[] buffer = new byte[fis.available()];
        int ignored = fis.read(buffer);
        fis.close();
        response.reset();
        // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
        response.addHeader("Content-Disposition", "attachment;filename=" +
                new String(filename.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));

        response.addHeader("Content-Length", "" + file.length());
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-onStreamProcessing");
        os.write(buffer);
        os.flush();
        os.close();
        commonResult.setFlag(true);
//        return commonResult;
    }

    @ResponseBody
    @RequestMapping(value = "jdfContent", method = RequestMethod.POST)
    public CommonResult jdfContent(@RequestParam(value = "jobId", required = true) String jobId) throws IOException {
        CommonResult result = new CommonResult(false);
        String path = SAVE_DIR + File.separator + "JOB_" + jobId + ".jdf";

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String line;

        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        result.setFlag(true);
        result.setResult(builder.toString());
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "showNodeRunLog", method = RequestMethod.POST)
    public CommonResult showNodeRunLog(@RequestParam(value = "nodeId", required = true) String nodeId,
                                       @RequestParam(value = "jobId", required = true) String jobId) throws IOException {

        CommonResult result = new CommonResult(false);
        String logDir = "C:\\eureka\\logs\\exe\\20171005";
        String path = logDir + File.separator + jobId + File.separator + nodeId + ".log";

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String line;

        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        result.setFlag(true);
        result.setResult(builder.toString());
        return result;
    }
}
