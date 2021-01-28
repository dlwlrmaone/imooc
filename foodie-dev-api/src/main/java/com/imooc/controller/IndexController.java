package com.imooc.controller;

import com.imooc.enumclass.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页展示Controller
 */
@Api(value = "首页",tags = "首页展示的相关接口")
@RestController
@RequestMapping("index")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取轮播图",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult getCarousel(){

        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);

        return IMOOCJSONResult.ok(carousels);
    }

    /**
     * 首页分类展示需求
     * 1.第一次刷新主页查询一级分类，渲染到首页
     * 2.如果鼠标移到一级分类上，则加载二级分类（懒加载模式）
     */
    @ApiOperation(value = "获取商品分类",notes = "获取商品分类（一级分类）",httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult getCategory(){

        List<Category> categories = categoryService.queryRootCategory();

        return IMOOCJSONResult.ok(categories);
    }

    /**
     * 根据一级分类ID查询二级分类列表
     * @param rootCatId
     * @return
     */
    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类（二级分类）",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult getSubCategory(
            @ApiParam(name = "rootCatId",value = "一级分类ID",required = true)
            @PathVariable Integer rootCatId){

        if (rootCatId == null){
            return IMOOCJSONResult.errorMsg("一级分类不存在！");
        }
        List<CategoryVO> categories = categoryService.getSubCatList(rootCatId);

        return IMOOCJSONResult.ok(categories);
    }

    /**
     * 获取首页6个商品并且懒加载显示
     * @param rootCatId
     * @return
     */
    @ApiOperation(value = "获取一级分类下最新6个商品",notes = "根据一级分类ID查询一级分类下最新6个商品",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult getNewSixItems(
            @ApiParam(name = "rootCatId",value = "一级分类ID",required = true)
            @PathVariable Integer rootCatId){

        if (rootCatId == null){
            return IMOOCJSONResult.errorMsg("一级分类不存在！");
        }
        List<NewItemsVO> items = categoryService.getSixNewItems(rootCatId);

        return IMOOCJSONResult.ok(items);
    }
}
