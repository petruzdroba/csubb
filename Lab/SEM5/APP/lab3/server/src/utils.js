export const jwtConfig = { secret: 'my-secret' };

export const revokedTokens = new Set();

export const isRevoked = async (ctx, decodedToken, token) => {
  return revokedTokens.has(token);
};

export const exceptionHandler = async (ctx, next) => {
  try {
    return await next();
  } catch (err) {
    console.log(err);
    ctx.body = { message: err.message || 'Unexpected error.' };
    ctx.status = err.status || 500;
  }
};

export const timingLogger = async (ctx, next) => {
  const start = Date.now();
  await next();
  console.log(`${ctx.method} ${ctx.url} => ${ctx.response.status}, ${Date.now() - start}ms`);
};