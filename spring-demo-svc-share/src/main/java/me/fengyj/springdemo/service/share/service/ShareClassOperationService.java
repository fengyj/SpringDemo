package me.fengyj.springdemo.service.share.service;

import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.common.exceptions.GeneralException;
import me.fengyj.common.utils.StringUtils;
import me.fengyj.springdemo.dao.ShareClassOperationDao;
import me.fengyj.springdemo.models.ShareClassOperation;
import me.fengyj.springdemo.utils.exceptions.DataIssueException;
import me.fengyj.springdemo.utils.exceptions.ResourceNotFoundException;
import me.fengyj.springdemo.utils.exceptions.UserInvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ShareClassOperationService {

    private final ShareClassOperationDao dao;

    public ShareClassOperationService(@Autowired ShareClassOperationDao dao) {
        this.dao = dao;
    }

    public List<ShareClassOperation> getList(String symbolOrName) {

        var list = dao.getAll();
        if (StringUtils.isNullOrWhiteSpace(symbolOrName)) return list;

        var criteria = symbolOrName.toLowerCase();
        return list.stream()
                   .filter(i -> (i.getName() != null && i.getName().toLowerCase().contains(criteria))
                           || (i.getSymbol() != null && i.getSymbol().toLowerCase().contains(criteria)))
                   .toList();
    }

    public ShareClassOperation get(String shareClassId) {

        if (StringUtils.isNullOrWhiteSpace(shareClassId))
            throw new GeneralException(ErrorSeverity.Error, "The shareClassId is not specified.", null);
        var share = dao.getByKey(shareClassId);
        if (share == null)
            throw new ResourceNotFoundException(ErrorSeverity.Error, ShareClassOperation.class, shareClassId, "The share cannot be found.");
        else
            return share;
    }

    public ShareClassOperation getBySymbol(String symbol) {

        var list = dao.getBySymbol(symbol);
        return switch (list.size()) {
            case 0 -> null;
            case 1 -> list.get(0);
            default -> throw new DataIssueException(
                    ErrorSeverity.Warning,
                    ShareClassOperation.class,
                    null,
                    "Symbol",
                    String.format("Found multiple items with the symbol: %s.", symbol));
        };
    }

    public ShareClassOperation update(ShareClassOperation data) {

        if (data == null)
            throw new UserInvalidInputException(ErrorSeverity.Error, null, "The data cannot be null.", null);
        if (StringUtils.isNullOrWhiteSpace(data.getShareClassId()))
            throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "shareClassId", "The ShareClassId cannot be null.");
        if (StringUtils.isNullOrWhiteSpace(data.getSymbol()))
            throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "symbol", "The Symbol cannot be null.");
        if (StringUtils.isNullOrWhiteSpace(data.getName()))
            throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "name", "The Name cannot be null.");

        dao.save(data);
        return dao.getByKey(data.getKey());
    }

    public ShareClassOperation add(ShareClassOperation data) {

        if (data == null)
            throw new UserInvalidInputException(ErrorSeverity.Error, null, "The data cannot be null.", null);
        if (!StringUtils.isNullOrWhiteSpace(data.getShareClassId()))
            throw new UserInvalidInputException(ErrorSeverity.Error, data.getShareClassId(), "The ShareClassId is not null.", null);
        if (StringUtils.isNullOrWhiteSpace(data.getSymbol()))
            throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "symbol", "The Symbol cannot be null.");
        if (StringUtils.isNullOrWhiteSpace(data.getName()))
            throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "name", "The Name cannot be null.");

        data.setShareClassId(generateShareClassId(dao.getShareClassIdSeed()));

        dao.save(data);
        return dao.getByKey(data.getKey());
    }

    public boolean delete(String shareClassId) {

        if (StringUtils.isNullOrWhiteSpace(shareClassId))
            throw new UserInvalidInputException(ErrorSeverity.Error, null, "The shareClassId is not specified.", null);
        return dao.delete(shareClassId);
    }

    private String generateShareClassId(int seed) {

        char[] chars = new char[10];
        Arrays.fill(chars, '0');
        chars[1] = 'P';

        for (int i = chars.length - 1; i >= 2; i--) {

            int v = seed % 36;
            seed = seed / 36;

            if (v < 10) {
                chars[i] = (char) ((int) '0' + v);
            } else {
                chars[i] = (char) ((int) 'A' + v - 10);
            }
        }
        return String.copyValueOf(chars);
    }
}
