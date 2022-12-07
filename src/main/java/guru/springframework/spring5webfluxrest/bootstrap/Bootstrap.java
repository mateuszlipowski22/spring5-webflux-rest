package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        if(categoryRepository.count().block()==0){
            loadCategories();
        }

        if(vendorRepository.count().block()==0){
            loadVendors();
        }

    }

    private void loadCategories() {

        categoryRepository.save(Category.builder().description("Fruits").build()).block();
        categoryRepository.save(Category.builder().description("Dried").build()).block();
        categoryRepository.save(Category.builder().description("Fresh").build()).block();
        categoryRepository.save(Category.builder().description("Exotic").build()).block();
        categoryRepository.save(Category.builder().description("Nuts").build()).block();

        System.out.println("Data loaded = " + categoryRepository.count().block());
    }

    private void loadVendors() {

        vendorRepository.save(Vendor.builder().firstName("Michale").lastName("Weston").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Sam").lastName("Axe").build()).block();

        System.out.println("Data loaded = " + vendorRepository.count().block());
    }

}
