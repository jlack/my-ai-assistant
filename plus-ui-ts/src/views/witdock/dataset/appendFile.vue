<template>
  <steps-add :datasetId="datasetId"/>
</template>

<script lang="ts" setup>
import {useRouter} from "vue-router";
import StepsAdd from "@/views/witdock/dataset/stepsAdd.vue";

const router = useRouter();
const datasetId = useRoute().params.id as string;
</script>
