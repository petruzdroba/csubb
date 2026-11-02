import axios from 'axios';
import { baseUrl, config, withLogs } from '../core';

const authUrl = `http://${baseUrl}/api/auth`;

export interface AuthProps {
  token: string;
}

export const login = (username?: string, password?: string): Promise<AuthProps> => {
  return withLogs(axios.post(`${authUrl}/login`, { username, password }, config), 'login');
};

export const logout = (token: string): Promise<void> => {
  return axios.post(`${authUrl}/logout`, {}, {
    headers: { Authorization: `Bearer ${token}` }
  });
};