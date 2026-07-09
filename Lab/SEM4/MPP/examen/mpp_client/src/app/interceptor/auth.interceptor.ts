import { Injectable } from '@angular/core';
import { HttpInterceptor } from '@angular/common/http';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: any, next: any) {

    const cloned = req.clone({
      withCredentials: true
    });

    return next.handle(cloned);
  }
}