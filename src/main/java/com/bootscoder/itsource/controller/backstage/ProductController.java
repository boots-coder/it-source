package com.bootscoder.itsource.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootscoder.itsource.bean.WangEditorResult;
import com.bootscoder.itsource.interceptor.ImageRepository2;
import com.bootscoder.itsource.pojo.Category;
import com.bootscoder.itsource.pojo.Image2;
import com.bootscoder.itsource.pojo.Product;
import com.bootscoder.itsource.service.CategoryService;
import com.bootscoder.itsource.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/backstage/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ImageRepository2 imageRepository;

    // 从配置文件中读取文件上传目录
    @Value("${file.upload.dir}")
    private String uploadDir;

    // 初始化文件存储路径
    private final Path fileStorageLocation;

    public ProductController(@Value("${file.upload.dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            // 创建文件夹
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("无法创建文件夹，原因: " + ex.getMessage());
        }
    }
    @RequestMapping("/all")
    public ModelAndView all(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5")int size){
        Page<Product> productPage = productService.findPage(page, size);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("productPage",productPage);
        modelAndView.setViewName("/backstage/product_all");
        return modelAndView;
    }

    @RequestMapping("/addPage")
    public ModelAndView addPage(){
        List<Category> categoryList = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categoryList",categoryList);
        modelAndView.setViewName("/backstage/product_add");
        return modelAndView;
    }

    @RequestMapping("/add")
    public String add(Product product){
        productService.add(product);
        return "redirect:/backstage/product/all";
    }

//    /**
//     * 这里的图片存储方案，有很大的问题，
//     1例如缓存会无限制积累，由于uuid 的存在并不会使得图片的删除而消失
//     2 这采用的路径地址为项目启动的路径，作为jar包部署后，由于jar包是一个压缩文件，无法写入
//     * @param file
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping("/upload")
//    @ResponseBody
//    public WangEditorResult upload(MultipartFile file) throws IOException {
//        // 创建文件夹，存放上传文件
//        // 1.设置上传文件夹的真实路径
//        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/upload";
//        System.out.println("真实上传图片路径" + realPath);
//        // 2.判断该文件夹是否存在，如果不存在，新建文件夹
//        File dir = new File(realPath);
//        if (!dir.exists()){
//            dir.mkdirs();
//        }
//        // 拿到上传文件名
//        String filename = file.getOriginalFilename();
//        filename = UUID.randomUUID()+filename;
//        // 创建空文件
//        File newFile = new File(dir, filename);
//        // 将上传的文件写到空文件中
//        file.transferTo(newFile);
//
//
//
//        // 构造返回结果
//        WangEditorResult wangEditorResult = new WangEditorResult();
//        wangEditorResult.setErrno(0);
//        String[] data = {"/upload/"+filename};
//        wangEditorResult.setData(data);
//        return wangEditorResult;
//    }

    @RequestMapping("/upload")
    @ResponseBody
//    @PostMapping("/upload")
    public WangEditorResult uploadImage2(MultipartFile file) {
        // 获取原始文件名
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 检查文件名是否包含无效字符
            if(originalFileName.contains("..")) {
                System.out.println("文件名不合法");
                return new WangEditorResult();
            }

            // 生成唯一的文件名
            String fileExtension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 目标位置
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

            // 将文件保存到目标位置
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 保存到数据库，使用唯一的文件名
            Image2 img = new Image2(originalFileName, file.getContentType(), targetLocation.toString());
            imageRepository.save(img);
            System.out.println("上传成功");

            System.out.println("图片存储路径"+img.getPath());
            System.out.println("图片存储名字"+img.getName());


//            return ResponseEntity.ok("图片上传成功，ID：" + img.getId());
            WangEditorResult wangEditorResult = new WangEditorResult();
            wangEditorResult.setErrno(0);
            String[] data = {"/" + uniqueFileName};
            System.out.println("回显的数据"+data[0]);
            wangEditorResult.setData(data);
            return wangEditorResult;
        } catch (IOException ex) {
            System.out.println("上传失败");
            return new WangEditorResult();
        }
    }


    @RequestMapping("/edit")
    public ModelAndView edit(Integer pid){
        // 查询被修改的产品
        Product product = productService.findOne(pid);
        // 查询所有产品类别
        List<Category> categoryList = categoryService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product",product);
        modelAndView.addObject("categoryList",categoryList);
        modelAndView.setViewName("/backstage/product_edit");
        return modelAndView;
    }

    @RequestMapping("/update")
    public String update(Product product){
        productService.update(product);
        return "redirect:/backstage/product/all";
    }

    @RequestMapping("/updateStatus")
    public String updateStatus(Integer pid,@RequestHeader("Referer") String referer){
        productService.updateStatus(pid);
        return "redirect:"+referer;
    }
}
