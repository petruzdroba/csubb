import React, { useContext } from 'react';
import { Redirect, Route } from 'react-router-dom';
import { AuthContext } from './AuthProvider';

export interface PrivateRouteProps {
  component: any;
  path: string;
  exact?: boolean;
}

export const PrivateRoute: React.FC<PrivateRouteProps> = ({ component: Component, ...rest }) => {
  const { isAuthenticated, isAuthReady } = useContext(AuthContext);

  return (
    <Route {...rest} render={props => {
      if (!isAuthReady) {
        return null;
      }

      if (isAuthenticated) {
        return <Component {...props} />;
      }

      return <Redirect to="/login" />;
    }}/>
  );
};