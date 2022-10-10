package az.uni.unitechapp.mapper;

import az.uni.unitechapp.dao.entity.Account;
import az.uni.unitechapp.model.response.AccountDetailResponse;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

    AccountDetailResponse toAccountDetailResponse(Account account);

}
