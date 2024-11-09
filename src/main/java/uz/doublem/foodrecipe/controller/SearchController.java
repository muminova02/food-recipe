package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.payload.RecipeDtoShow;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.service.SearchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> getResentSearch(@RequestParam Integer size,@RequestParam Integer page, @RequestParam Integer userId) {
        ResponseMessage res = searchService.getResentSearch(size,page,userId);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping("/result")
    public ResponseEntity<?> searchResult(@RequestBody RecipeDtoShow recipeDtoShow) {
        ResponseMessage res = searchService.getSearchResult(recipeDtoShow);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


}
