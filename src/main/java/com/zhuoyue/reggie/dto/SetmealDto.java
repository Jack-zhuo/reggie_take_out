package com.zhuoyue.reggie.dto;

import com.zhuoyue.reggie.entity.Setmeal;
import com.zhuoyue.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
