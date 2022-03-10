import Layout from "../components/layout";
import {useSession} from "next-auth/react";
import React, { useState, useEffect } from 'react';

export default function Protected() {

    const { data: session, status } = useSession()

    if (status === "unauthenticated") {
        return <Layout><p>Access Denied</p></Layout>
    }

    return (
        <Layout>
        <h1>Protected Page</h1>
    <p>You can view this page because you are signed in.</p>
    </Layout>
)
}