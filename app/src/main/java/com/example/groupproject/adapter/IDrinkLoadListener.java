package com.example.groupproject.adapter;

import java.util.List;

public interface IDrinkLoadListener {
    void onDrinkLoadSuccess(List<DrinkModel> drinkModelList);
    void onDrinkLoadFailed(String message);

    void onCartLoadSuccess(List<CartModel> cartModelList);

    void onCartLoadFailed(String message);
}
