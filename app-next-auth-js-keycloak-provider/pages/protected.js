import Layout from "../components/layout";
import {useSession} from "next-auth/react";

export default function Protected() {

    const { data: session, status } = useSession()

    return (
        <Layout>
            You are not authorize to see the content
        </Layout>
);
}