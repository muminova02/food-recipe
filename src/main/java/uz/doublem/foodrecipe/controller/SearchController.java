package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.RecipeDtoShow;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.search.SearchDto;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.SearchService;
import uz.doublem.foodrecipe.util.Util;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getResentSearch(@RequestParam Integer size,@RequestParam Integer page, @RequestParam Integer userId) {
        ResponseMessage res = searchService.getResentSearch(size,page,userId);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping("/result")
    public ResponseEntity<?> getSearchResult(@RequestBody SearchDto searchDto,@RequestParam Integer size,@RequestParam Integer page) {
        ResponseMessage res = searchService.getSearchResult(searchDto,size,page);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @GetMapping("/result/{id}")
    public ResponseEntity<Void> getRecipeInSearchResult(@PathVariable Integer id) {
        User user = Util.getCurrentUser();
        searchService.createRecentSearch(id,user);

        URI redirectUri = URI.create("/api/recipeM/" + id);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(redirectUri).build();
    }

}
