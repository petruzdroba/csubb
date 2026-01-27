package org.example.service;

import org.example.domain.MenuItem;
import org.example.repo.MenuItemRepo;

import java.util.List;

public class MenuService {
    private final MenuItemRepo menuRepo;

    public MenuService(MenuItemRepo menuRepo) {
        this.menuRepo = menuRepo;
    }

    public List<MenuItem> getAll() {
        return menuRepo.getAll();
    }

    public List<String> getCategories() {
        return menuRepo.getCategories();
    }

    public List<MenuItem> getByCategory(String category) {
        return menuRepo.getByCategory(category);
    }


}
