package com.bootscoder.itsource.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootscoder.itsource.bean.WangEditorResult;
import com.bootscoder.itsource.pojo.Category;
import com.bootscoder.itsource.pojo.Product;
import com.bootscoder.itsource.service.CategoryService;
import com.bootscoder.itsource.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/backstage/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

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

    /**
     * 这里的图片存储方案，有很大的问题，例如缓存会无限制积累，由于uuid 的存在并不会使得图片的删除而消失
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    @ResponseBody
    public WangEditorResult upload(HttpServletRequest request,
                                   MultipartFile file) throws IOException {
        // 创建文件夹，存放上传文件
        // 1.设置上传文件夹的真实路径
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/upload/img/product/small/";
        // 定义io流的相对路径
        String relativePath = "src/main/resources/static/upload/img/product/small";

        // 创建路径对象并检查目录是否存在
        Path pathWrite = Paths.get(relativePath);
        if (!Files.exists(pathWrite)) {
            Files.createDirectories(pathWrite);
        }
        // 2.判断该文件夹是否存在，如果不存在，新建文件夹
        File dir = new File(realPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        // 拿到上传文件名
        String filename = file.getOriginalFilename();
        filename = UUID.randomUUID()+filename;
        // 生成io流需要的完整文件路径
        File fileWrite = new File(pathWrite.toString(), filename);

        // 创建空文件
        File newFile = new File(dir, filename);
        // 创建io流需要的文件并写入到本地
        try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
            fos.write(file.getBytes());
        }
        // 将上传的文件写到空文件中
        file.transferTo(newFile);
        // 构造返回结果
        WangEditorResult wangEditorResult = new WangEditorResult();
        wangEditorResult.setErrno(0);
        String[] data = {"/upload/img/product/small/"+filename};
        wangEditorResult.setData(data);
        return wangEditorResult;
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
