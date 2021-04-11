<script>
	import { Select } from 'svelte-materialify/src';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	export let value = "";
  export let errorCount = 1;
  export let error = false;

  $: {
    dispatch('change', value);
    validate(value);
  };

  let errorMessages = [];
  export let rules = [];
  export function validate() {
    errorMessages = rules.map((r) => r(value)).filter((r) => typeof r === 'string');
    if (errorMessages.length) error = true;
    else {
      error = false;
    }
    return error;
  }
</script>

<div
  class="vl-select {error ? 'error error-text' : ''}">

<Select { ...$$props } bind:value>
  <slot />
</Select>
<div class="s-input__details">
  {#each errorMessages.slice(0, errorCount) as message}
    <span>{message}</span>
  {/each}
</div>
</div>

<style>
  :global(button) {
    cursor: pointer;
  }

  :global(.vl-select.error.error-text .s-input) {
    margin-bottom: 0;
  }

  .s-input__details {
    margin-bottom: 1rem;
  }
</style>