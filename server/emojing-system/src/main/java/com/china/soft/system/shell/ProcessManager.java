package com.china.soft.system.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {

    private static final String MEFRPC_COMMAND = "./mefrpc";
    private static final String TOKEN = "b275f77317aa8a3d88c327a5e53d657f";

    public static String findProcessPid() throws IOException {
        // 构造查找进程的命令（精准匹配token和port，避免误杀）
        List<String> cmd = new ArrayList<>();
        cmd.add("sh");          // 调用Shell解释器
        cmd.add("-c");          // 告诉Shell执行后续的命令字符串
        cmd.add("ps aux | grep 'mefrp' | grep -v grep | awk '{print $2}'");
        Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line.trim());
            }
        }
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        // 退出码0且有输出则返回PID，否则返回null
        return exitCode == 0 && !output.toString().isEmpty() ? output.toString() : null;
    }


    /**
     * 终止指定PID的进程
     */
    public static boolean killProcess(String pid) throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add("kill");
        cmd.add("-9"); // 强制终止
        cmd.add(pid);

        Process process = new ProcessBuilder(cmd).start();
        try {
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 启动新的mefrpc进程（nohup后台运行）
     */
    public static void startNewProcess(String param1,String param2) throws IOException {
        // 构造nohup命令（后台运行，输出重定向到日志文件）
        String cmd = String.format("nohup ./mefrpc -t %s -p %s > mefrpc.log 2>&1 &", param1, param2);
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", cmd);
        pb.directory(new java.io.File("/opt/mefrp"));
        // 启动进程（后台运行，无需等待）
        pb.start();
    }
}
