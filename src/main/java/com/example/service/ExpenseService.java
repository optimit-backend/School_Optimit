package com.example.service;

import com.example.entity.Balance;
import com.example.entity.Branch;
import com.example.entity.Expense;
import com.example.entity.User;
import com.example.exception.RecordNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.common.ApiResponse;
import com.example.model.request.ExpenseRequestDto;
import com.example.model.response.ExpenseResponse;
import com.example.repository.BalanceRepository;
import com.example.repository.BranchRepository;
import com.example.repository.ExpenseRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.enums.Constants.*;

@Service
@RequiredArgsConstructor
public class ExpenseService implements BaseService<ExpenseRequestDto, Integer> {

    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public ApiResponse create(ExpenseRequestDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RecordNotFoundException(BRANCH_NOT_FOUND));
        Balance balance = balanceRepository.findByBranchId(dto.getBranchId())
                .orElseThrow(() -> new RecordNotFoundException(BALANCE_NOT_FOUND));
        User user = userRepository.findById(dto.getTakerId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if (balance.getBalance() < dto.getSumma()) {
            throw new RecordNotFoundException(BALANCE_NOT_ENOUGH_SUMMA);
        }
        balance.setBalance(balance.getBalance() - dto.getSumma());
        Expense expense = Expense.builder()
                .summa(dto.getSumma())
                .reason(dto.getReason())
                .createdTime(LocalDateTime.now())
                .taker(user)
                .branch(branch)
                .build();
        expenseRepository.save(expense);
        balanceRepository.save(balance);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @Override
    public ApiResponse getById(Integer integer) {
        return null;
    }

    public ApiResponse getAllByBranchId(Integer branchId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Expense> all = expenseRepository.findAllByBranchIdAndCreatedTimeBetweenOrderByCreatedTimeDesc(branchId, startTime, endTime);
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        all.forEach(expense -> expenseResponseList.add(ExpenseResponse.from(expense)));
        return new ApiResponse(expenseResponseList, true);
    }

    @Override
    public ApiResponse update(ExpenseRequestDto dto) {
        Expense expense = expenseRepository.findById(dto.getId()).orElseThrow(() -> new RecordNotFoundException(EXPENSE_NOT_FOUND));
        Balance balance = balanceRepository.findByBranchId(dto.getBranchId())
                .orElseThrow(() -> new RecordNotFoundException(BALANCE_NOT_FOUND));
        User user = userRepository.findById(dto.getTakerId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if (dto.getSumma() > expense.getSumma()) {
            double v = dto.getSumma() - expense.getSumma();
            if (balance.getBalance() >= v) {
                balance.setBalance(balance.getBalance() - v);
                balanceRepository.save(balance);
            } else {
                throw new RecordNotFoundException(BALANCE_NOT_ENOUGH_SUMMA);
            }
        } else if (dto.getSumma() < expense.getSumma()) {
            double v = expense.getSumma() - dto.getSumma();
            balance.setBalance(balance.getBalance() + v);
            balanceRepository.save(balance);
        }
        expense.setTaker(user);
        expense.setReason(dto.getReason());
        expense.setSumma(dto.getSumma());
        expenseRepository.save(expense);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @Override
    public ApiResponse delete(Integer integer) {
        return null;
    }
}