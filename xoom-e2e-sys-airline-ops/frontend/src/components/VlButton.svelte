<script>
	import { Icon } from 'svelte-materialify/src';
	import MatButton from 'svelte-materialify/src/components/Button';
	import { createEventDispatcher } from 'svelte';
	import { goto } from '@sapper/app';

	const dispatch = createEventDispatcher();

	export let text = "";
	export let outlined = false;
	export let icon = "";
	export let href = "";
	export let disabled = false;

	function clicked() {
		if (href) {
			goto(href);
		} else {
			dispatch("click");
		}
	}
</script>

<MatButton class={"m-1 "} {...$$restProps}  on:click={clicked} {outlined} disabled={disabled}>
	<span>
		{#if icon}
			<Icon path={icon}/>
		{/if}
		{text}
	</span>
</MatButton>

<style>
  :global(button) {
    cursor: pointer;
  }
</style>