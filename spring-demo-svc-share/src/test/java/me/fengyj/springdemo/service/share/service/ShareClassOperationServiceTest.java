package me.fengyj.springdemo.service.share.service;

import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.common.exceptions.GeneralException;
import me.fengyj.springdemo.dao.ShareClassOperationDao;
import me.fengyj.springdemo.models.ShareClassOperation;
import me.fengyj.springdemo.utils.exceptions.DataIssueException;
import me.fengyj.springdemo.utils.exceptions.UserInvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShareClassOperationServiceTest {
    @Mock
    private ShareClassOperationDao dao;

    @InjectMocks
    private ShareClassOperationService service;

    @Test
    void getList() {

        var expected = getMockData();

        when(dao.getAll()).thenReturn(expected);

        var actual = service.getList(null);

        assertEquals(expected, actual);

        actual = service.getList("IBM");

        assertEquals(Collections.singletonList(expected.get(0)), actual);
    }

    @Test
    void get() {

        var expected = getMockData();

        when(dao.getByKey(expected.get(1).getKey())).thenReturn(expected.get(1));

        var actual = service.get(expected.get(1).getShareClassId());

        assertEquals(expected.get(1), actual);
    }

    @Test
    void get_with_null_input() {

        var actual = assertThrows(GeneralException.class, () -> service.get(null));

        assertEquals(ErrorSeverity.Error, actual.getLevel());
    }

    @Test
    void getBySymbol() {

        var expected = getMockData();

        when(dao.getBySymbol(expected.get(1).getSymbol())).thenReturn(Collections.singletonList(expected.get(1)));

        var actual = service.getBySymbol(expected.get(1).getSymbol());

        assertEquals(expected.get(1), actual);
    }

    @Test
    void getBySymbol_with_duplicated_symbol() {

        var data = getMockData();
        data.get(1).setSymbol(data.get(0).getSymbol());
        var expected = data.stream().limit(2).toList();

        when(dao.getBySymbol(expected.get(1).getSymbol())).thenReturn(expected);

        var actual = assertThrows(DataIssueException.class, () -> service.getBySymbol(expected.get(1).getSymbol()));

        assertEquals(ErrorSeverity.Warning, actual.getLevel());
    }

    @Test
    void add() {

        var expected = getMockData().get(0);

        doNothing().when(dao).save(expected);
        when(dao.getByKey(expected.getKey())).thenReturn(expected);

        var actual = service.update(expected);

        assertEquals(expected, actual);

        expected = new ShareClassOperation();
        expected.setSymbol("Symbol");
        expected.setName("Name");

        var expectedId = "0P0000002S";

        doNothing().when(dao).save(expected);
        when(dao.getByKey(expectedId)).thenReturn(expected);
        when(dao.getShareClassIdSeed()).thenReturn(100);

        actual = service.add(expected);

        assertEquals(expectedId, actual.getShareClassId());
    }

    @Test
    void update_with_null_input() {

        var actual = assertThrows(UserInvalidInputException.class, () -> service.update(null));

        assertEquals(ErrorSeverity.Error, actual.getLevel());
    }

    @Test
    void delete() {

        var data = getMockData().get(0);
        var expected = true;

        when(dao.delete(data.getKey())).thenReturn(true);

        var actual = service.delete(data.getShareClassId());

        assertEquals(expected, actual);
    }

    private List<ShareClassOperation> getMockData() {

        return Arrays.asList(
                createObject("0P00000001", "International Business Machine", "NAS:IBM"),
                createObject("0P00000002", "Microsoft", "NAS:MSFT"),
                createObject("0P00000003", "Apple", "NAS:APPL"));
    }

    private ShareClassOperation createObject(String id, String name, String symbol) {

        var obj = new ShareClassOperation();
        obj.setShareClassId(id);
        obj.setName(name);
        obj.setSymbol(symbol);
        obj.setCreatedTime(LocalDateTime.now());
        obj.setLastUpdatedTime(obj.getCreatedTime());

        return obj;
    }

}