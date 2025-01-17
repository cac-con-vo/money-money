package com.example.moneymoney.service.Impl;

import com.example.moneymoney.entity.Asset;
import com.example.moneymoney.entity.Income;
import com.example.moneymoney.entity.IncomeCategory;
import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.IncomeRequestModel;
import com.example.moneymoney.model.responsemodel.IncomeResponse;
import com.example.moneymoney.repository.AssetRepository;
import com.example.moneymoney.repository.IncomeCategoryRepository;
import com.example.moneymoney.repository.IncomeRepository;
import com.example.moneymoney.service.AssetService;
import com.example.moneymoney.service.IncomeCategoryService;
import com.example.moneymoney.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;

    private final IncomeCategoryRepository incomeCategoryRepository;

    private final AssetService assetService;

    private final IncomeCategoryService incomeCategoryService;


    @Override
    public IncomeResponse createIncome(IncomeRequestModel incomeRequest, User loggedInUser) {
        List<IncomeCategory> incomeCategories = incomeCategoryService.getAllIncomeCategories();
        List<Asset> assets = assetService.getAllAssets();

        if (incomeCategories.isEmpty()) {
            throw new IllegalArgumentException("Income categories cannot be empty");
        }

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("Assets cannot be empty");
        }

        // Lấy Income Category đầu tiên từ danh sách Income Categories
        IncomeCategory incomeCategory = incomeCategories.get(0);

        // Lấy Asset đầu tiên từ danh sách Assets
        Asset asset = assets.get(0);

        Income income = new Income();
        income.setUser(loggedInUser);
        income.setIncomeCategory(incomeCategory);
        income.setDate(incomeRequest.getDate());
        income.setAmount(incomeRequest.getAmount());
        income.setDescription(incomeRequest.getDescription());
        income.setAsset(asset);

        Income createdIncome = incomeRepository.save(income);

        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.setIncomeCategoryName(createdIncome.getIncomeCategory().getIncomeCategoryName());
        incomeResponse.setDate(createdIncome.getDate());
        incomeResponse.setAmount(createdIncome.getAmount());
        incomeResponse.setDescription(createdIncome.getDescription());
        incomeResponse.setAssetName(createdIncome.getAsset().getAssetName());

        return incomeResponse;
    }
    @Override
    public IncomeResponse getIncomeById(Long incomeId, User loggedInUser) {
        Income income = incomeRepository.findByIdAndUser(incomeId, loggedInUser)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.setIncomeCategoryName(income.getIncomeCategory().getIncomeCategoryName());
        incomeResponse.setDate(income.getDate());
        incomeResponse.setAmount(income.getAmount());
        incomeResponse.setDescription(income.getDescription());
        incomeResponse.setAssetName(income.getAsset().getAssetName());
        return incomeResponse;
    }

    @Override
    public IncomeResponse updateIncome(Long incomeId, IncomeRequestModel incomeRequest, User loggedInUser) {
        Income existingIncome = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        List<IncomeCategory> incomeCategories = incomeCategoryService.getAllIncomeCategories();
        List<Asset> assets = assetService.getAllAssets();

        if (incomeCategories.isEmpty()) {
            throw new IllegalArgumentException("Income categories cannot be empty");
        }

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("Assets cannot be empty");
        }

        // Lấy Income Category đầu tiên từ danh sách Income Categories
        IncomeCategory incomeCategory = incomeCategories.get(0);

        // Lấy Asset đầu tiên từ danh sách Assets
        Asset asset = assets.get(0);

        existingIncome.setIncomeCategory(incomeCategory);
        existingIncome.setDate(incomeRequest.getDate());
        existingIncome.setAmount(incomeRequest.getAmount());
        existingIncome.setDescription(incomeRequest.getDescription());

        if (existingIncome.getAsset() == null) {
            Asset newAsset = new Asset();
            newAsset.setAssetName(asset.getAssetName());
            existingIncome.setAsset(newAsset);
        } else {
            existingIncome.getAsset().setAssetName(asset.getAssetName());
        }

        Income updatedIncome = incomeRepository.save(existingIncome);

        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.setIncomeCategoryName(updatedIncome.getIncomeCategory().getIncomeCategoryName());
        incomeResponse.setDate(updatedIncome.getDate());
        incomeResponse.setAmount(updatedIncome.getAmount());
        incomeResponse.setDescription(updatedIncome.getDescription());
        incomeResponse.setAssetName(updatedIncome.getAsset().getAssetName());

        return incomeResponse;
    }

    @Override
    public void deleteIncome(Long incomeId, User loggedInUser) {
        Income income = incomeRepository.findByIdAndUser(incomeId,loggedInUser)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        incomeRepository.delete(income);
    }

    @Override
    public List<Income> getListIncome(User loggedInUser) {
        return incomeRepository.findAllByUser(loggedInUser);
    }






    @Override
    public BigDecimal getTotalAmountByDay(Date date, User loggedInUser) {
        return incomeRepository.getTotalAmountByDay(date,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByWeek(Date date, User loggedInUser) {
        return incomeRepository.getTotalAmountByWeek(date,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByMonth(Date date, User loggedInUser) {
        return incomeRepository.getTotalAmountByMonth(date,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByYear(int year, User loggedInUser) {
        return incomeRepository.getTotalAmountByYear(year,loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByDays(User loggedInUser) {
        Date currentDate = new Date();
        return incomeRepository.getTotalAmountByDay(currentDate, loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByWeeks(User loggedInUser) {
        Date currentDate = new Date();
        return incomeRepository.getTotalAmountByWeek(currentDate, loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByMonths(User loggedInUser) {
        Date currentDate = new Date();
        return incomeRepository.getTotalAmountByMonth(currentDate, loggedInUser);
    }

    @Override
    public BigDecimal getTotalAmountByYears(User loggedInUser) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return incomeRepository.getTotalAmountByYear(currentYear, loggedInUser);
    }
}