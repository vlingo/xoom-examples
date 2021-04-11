<script>
	import { Icon, ListItem } from "svelte-materialify/src";

	export let item;
	export let segment;
	$: active = segment ? segment === item.href : item.href === "";
</script>

<a href={item.href} rel="prefetch">
	<ListItem active={active} dense> <!-- activeClass="nav-active-link" -->
		<span slot="prepend">
			{#if item.icon}
				<Icon path={active ? item.openIcon : item.icon} />
			{/if}
		</span>
		{item.text}
	</ListItem>
</a>
