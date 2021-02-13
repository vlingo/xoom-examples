<script>
	import { Select } from 'svelte-materialify/src';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	export let value = "";

  $: {
    if (value) {
      dispatch('change', value)
    }
  }
</script>

<Select { ...$$props } bind:value />

<style>
  :global(button) {
    cursor: pointer;
  }
</style>