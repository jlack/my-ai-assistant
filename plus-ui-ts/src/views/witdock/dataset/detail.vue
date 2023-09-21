<template>
  <div>详情{{ id }}</div>
</template>


<script setup lang="ts">

const id = useRoute().params.id;
</script>


<style scoped lang="scss">

</style>
