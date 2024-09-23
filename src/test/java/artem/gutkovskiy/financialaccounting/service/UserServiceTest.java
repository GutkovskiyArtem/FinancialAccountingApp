package artem.gutkovskiy.financialaccounting.service;

import artem.gutkovskiy.financialaccounting.entity.User;
import artem.gutkovskiy.financialaccounting.repository.UserRepository;
import artem.gutkovskiy.financialaccounting.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private Cache<User> userCache;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userCache = mock(Cache.class);
        userService = new UserService(userRepository, userCache);
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        when(userCache.containsKey("all_users")).thenReturn(false);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        doNothing().when(userCache).put(any(String.class), any());

        var result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
        verify(userCache).put("all_users", Arrays.asList(user));
    }

    @Test
    void findAll_shouldReturnCachedUsers_whenCacheExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Cached User");

        when(userCache.containsKey("all_users")).thenReturn(true);
        when(userCache.get("all_users")).thenReturn(Arrays.asList(user));

        var result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Cached User", result.get(0).getName());
        verify(userRepository, never()).findAll();
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var result = userService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void save_shouldSaveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        when(userRepository.save(any(User.class))).thenReturn(user);

        var savedUser = userService.save(user);

        assertEquals(user, savedUser);
        verify(userCache).invalidate("all_users");
        verify(userCache).invalidate("1");
    }

    @Test
    void deleteById_shouldDeleteUser_whenExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userCache).invalidate("all_users");
        verify(userCache).invalidate("1");
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldNotDelete_whenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userService.deleteById(1L);

        verify(userCache, never()).invalidate(anyString());
        verify(userRepository, never()).deleteById(anyLong());
    }
    @Test
    void findById_shouldReturnCachedUser_whenExistsInCache() {
        User user = new User();
        user.setId(1L);
        when(userCache.containsKey("1")).thenReturn(true);
        when(userCache.get("1")).thenReturn(Collections.singletonList(user));

        var result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void findAll_shouldReturnEmpty_whenNoUsersFound() {
        when(userCache.containsKey("all_users")).thenReturn(false);
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        var result = userService.findAll();

        assertTrue(result.isEmpty());
        verify(userCache, never()).put(anyString(), any());
    }

}
