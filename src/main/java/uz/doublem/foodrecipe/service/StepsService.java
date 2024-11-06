package uz.doublem.foodrecipe.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.Category;
import uz.doublem.foodrecipe.entity.Step;
import uz.doublem.foodrecipe.payload.CategoryDto;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.StepRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepsService {
    private final StepRepository stepRepository;


}
