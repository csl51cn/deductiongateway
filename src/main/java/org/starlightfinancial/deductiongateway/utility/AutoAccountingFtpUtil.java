package org.starlightfinancial.deductiongateway.utility;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.util.Properties;

/**
 * @author: Senlin.Deng
 * @Description: 自动入账Ftp工具类
 * @date: Created in 2018/7/12 16:09
 * @Modified By:
 */
public class AutoAccountingFtpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoAccountingFtpUtil.class);


    /**
     * ftp服务器地址
     */
    private static String server;
    /**
     * 端口
     */
    private static int port;
    /**
     * 用户名
     */
    private static String username;
    /**
     * 密码
     */
    private static String password;

    static{
        Properties properties = new Properties();
        InputStream resourceAsStream = AutoAccountingFtpUtil.class.getResourceAsStream("/ftp.properties");
        try {
            properties.load(resourceAsStream);
            server=(String) properties.get("ftp.auto_accounting.server");
            port = Integer.parseInt((String)properties.get("ftp.auto_accounting.port")) ;
            username = (String) properties.get("ftp.auto_accounting.username");
            password = (String) properties.get("ftp.auto_accounting.password");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("加载自动入账FTP配置失败.");
        }finally {
            IOUtils.closeQuietly(resourceAsStream);
        }
    }




    /**
     * 上传文件
     *
     * @param remoteFileName 远程文件名称
     * @param localFileName   本地文件名称
     */
    public static void  upload(String remoteFileName, String localFileName) throws Exception {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //上传文件
            upload(ftp, remoteFileName, localFileName);
            //退出
            logout(ftp);
            LOGGER.info("上传自动入账文件成功,文件名:{}",remoteFileName);
        } finally {
            closeFtpClient(ftp);
        }

    }

    /**
     * 关闭 FTPClient
     * @param ftp
     */
    private static void closeFtpClient(FTPClient ftp) {
        if ( ftp != null) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    LOGGER.error("关闭FTPClient出错", e);
                }
            }
        }
    }


    /**
     * 上传文件
     *
     * @param remoteFileName 远程文件名称
     * @param inputStream   流
     */
    public static void  upload(String remoteFileName, InputStream inputStream) throws IOException {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
//            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //上传文件
            upload(ftp, remoteFileName, inputStream);
            //退出
            logout(ftp);
            LOGGER.info("上传自动入账文件成功,文件名:{}",remoteFileName);
        } finally {
            closeFtpClient(ftp);
        }

    }

    /**
     * 下载文件
     *
     * @param remoteFileName 远程文件名称
     * @param localFileName   本地文件名称
     */
    public static void download(String remoteFileName, String localFileName) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //下载文件
            download(ftp, remoteFileName, localFileName);
            //退出
            logout(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeFtpClient(ftp);

        }
    }

    /**
     * 创建文件夹
     *
     * @param remotePathName 远程文件夹名称
     */
    public static void makeDirectory(String remotePathName) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            //连接ftp服务器
            connect(ftp);
            //设置属性
            setProperty(ftp);
            //创建文件夹
            makeDirectory(ftp, remotePathName);
            //退出
            logout(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeFtpClient(ftp);
        }
    }

    /**
     * @param ftp
     * @param remotePathName
     */
    private static void makeDirectory(FTPClient ftp, String remotePathName) throws Exception {
        ftp.makeDirectory(remotePathName);
    }

    /**
     * @param ftp
     * @param remoteFileName
     * @param localFileName
     */
    private static void download(FTPClient ftp, String remoteFileName,
                                 String localFileName) throws Exception {
        OutputStream output = null;
        output = new FileOutputStream(localFileName);
        ftp.retrieveFile(remoteFileName, output);
        output.close();
    }

    /**
     * @param ftp FTPClient
     * @throws Exception
     */
    private static void setProperty(FTPClient ftp) throws IOException {
        ftp.enterLocalPassiveMode();
        //二进制传输,默认为ASCII
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
    }

    /**
     * @param ftp
     */
    private static void logout(FTPClient ftp) throws IOException {
        ftp.noop();
        ftp.logout();
    }

    /**
     * @param ftp
     * @param remoteFileName
     * @param localFileName
     */
    private static void upload(FTPClient ftp, String remoteFileName,
                        String localFileName) throws Exception {
        //上传
        InputStream input;

        input = new FileInputStream(localFileName);

        ftp.storeFile(remoteFileName, input);

        input.close();
    }


    /**
     * @param ftp
     * @param remoteFileName
     * @param inputStream
     */
    private static void upload(FTPClient ftp, String remoteFileName,
                               InputStream inputStream) throws IOException {
        //上传
        ftp.storeFile(remoteFileName, inputStream);

        inputStream.close();
    }

    /**
     * @param ftp
     */
    private static void connect(FTPClient ftp) throws IOException {
        //连接服务器
        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        //是否连接成功
        if (!FTPReply.isPositiveCompletion(reply)) {
            LOGGER.error(server + " 服务器拒绝连接");
            throw new ConnectException(server + " 服务器拒绝连接");
        }
        //登陆
        if (!ftp.login(username, password)) {
            LOGGER.error("用户名或密码错误");
            throw new ConnectException("用户名或密码错误");
        }
    }


}
