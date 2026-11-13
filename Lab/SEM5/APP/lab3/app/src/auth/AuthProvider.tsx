import React, { useCallback, useEffect, useState } from 'react';
import { getLogger } from '../core';
import { login as loginApi, logout as logoutApi } from './authApi';

const log = getLogger('AuthProvider');
const TOKEN_KEY = 'authToken';

type LoginFn = (username?: string, password?: string) => Promise<void>;

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  isAuthReady: boolean;
  login?: LoginFn;
  logout?: () => Promise<void>;
  token: string;
}

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  isAuthReady: false,
  authenticationError: null,
  token: '',
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);

  useEffect(() => {
    const token = localStorage.getItem(TOKEN_KEY) || '';

    setState({
      ...initialState,
      token,
      isAuthenticated: !!token,
      isAuthReady: true,
    });
  }, []);

  const login = useCallback<LoginFn>(async (username, password) => {
    setState(prev => ({
      ...prev,
      isAuthenticating: true,
      authenticationError: null,
    }));

    try {
      const { token } = await loginApi(username, password);

      localStorage.setItem(TOKEN_KEY, token);

      setState({
        ...initialState,
        token,
        isAuthenticated: true,
        isAuthReady: true,
      });
    } catch (error) {
      setState(prev => ({
        ...prev,
        authenticationError: error as Error,
        isAuthenticating: false,
      }));
    }
  }, []);

  const logout = useCallback(async () => {
    const token = localStorage.getItem(TOKEN_KEY);

    try {
      if (token) {
        await logoutApi(token);
      }
    } finally {
      localStorage.removeItem(TOKEN_KEY);

      setState({
        ...initialState,
        isAuthReady: true,
      });
    }
  }, []);

  const value = {
    ...state,
    login,
    logout,
  };

  log('render');

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};