package com.ittry.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > 100 * 1024 * 1024) return "文件过大";
        String originName = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(originName);
        String filename = UUID.randomUUID() + "." + ext;
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(UPLOAD_DIR + filename));
        java.util.Map<String, String> resp = new java.util.HashMap<>();
        resp.put("originName", originName);
        resp.put("filename", filename);
        return resp;
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response) throws IOException {
        File file = new File(UPLOAD_DIR + filename);
        if (!file.exists()) {
            response.setStatus(404);
            return;
        }
        response.setContentType(Files.probeContentType(Paths.get(file.getAbsolutePath())));
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        try (InputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }
}