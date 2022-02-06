import NextAuth from "next-auth"
import KeycloakProvider from "next-auth/providers/keycloak";
import axios from "axios";
/*
* @ Thanks
* https://github.com/nextauthjs/next-auth-refresh-token-example
*/
const refreshAccessToken = async (token) => {
    console.log('refresh token!');
    try {
        const response = await axios.post(
            `${process.env.NEXT_PUBLIC_KEYCLOAK_ISSUER}/protocol/openid-connect/token`,
            new URLSearchParams({
                client_id: `${process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID}`,
                client_secret:  `${process.env.NEXT_PUBLIC_KEYCLOAK_SECRET}`,
                grant_type: "refresh_token",
                refresh_token: token.refreshToken,
            })
        );

        const refreshedTokens = response.data;
        console.log(refreshedTokens);

        if (response.status !== 200) {
            throw refreshedTokens;
        }

        return {
            ...token,
            accessToken: refreshedTokens.access_token,
            accessTokenExpires: Date.now() + refreshedTokens.expires_in * 1000,
            refreshToken: refreshedTokens.refresh_token ?? token.refreshToken, // Fall back to old refresh token
        };
    } catch (error) {
        console.log(error)
        return {
            ...token,
            error: "RefreshAccessTokenError",
        };
    }
};


export default NextAuth({
    providers: [
        KeycloakProvider({
            clientId: process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID,
            clientSecret: process.env.NEXT_PUBLIC_KEYCLOAK_SECRET,
            issuer: process.env.NEXT_PUBLIC_KEYCLOAK_ISSUER,
        }),
    ],
    secret: process.env.NEXTAUTH_SECRET,
    debug: true,
    //encryption: false,
    callbacks: {

        // https://github.com/nextauthjs/next-auth-refresh-token-example/blob/main/pages/api/auth/%5B...nextauth%5D.js
        async jwt({ token, user, account }) {

           // console.log('Refresh token',token)
            //console.log('Refresh user',user)
            //console.log('Refresh account',account)

            //token.refreshToken = account?.refresh_token;
            // Initial sign in
            if (account && user) {


                return {
                    accessToken: account.access_token,
                    accessTokenExpires: Date.now() + account.expires_in * 1000,
                    refreshToken: account.refresh_token,
                    //abcToken: token.refresh_token,
                    user
                }
            }

            // Return previous token if the access token has not expired yet
            if (Date.now() < token.accessTokenExpires) {

                return token
            }

            // Access token has expired, try to update it
            return  refreshAccessToken(token);
        },
        async session({ session, token }) {
            session.user = token.user
            session.accessToken = token.accessToken
            session.refreshToken = token.refreshToken
            session.error = token.error

            return session
        }
    },
})
