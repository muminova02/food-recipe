package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public RedirectView getRecipeInSearchResult(@PathVariable Integer id) {
//        User curentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(2);
        searchService.createRecentSearch(id,byId.get());
        return new RedirectView("api/recipe/"+id);
    }

}
