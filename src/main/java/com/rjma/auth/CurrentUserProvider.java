package com.rjma.auth;

import com.rjma.entity.Usuario;

public interface CurrentUserProvider {

    Usuario getCurrentUser();
}
