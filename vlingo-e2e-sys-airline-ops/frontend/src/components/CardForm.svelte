<script>
	import { mdiChevronLeft, mdiChevronRight } from '@mdi/js';
	import { Card, CardActions, CardText, CardTitle, Row, Col, Alert, Button, Dialog} from 'svelte-materialify/src';
	import VlButton from './VlButton.svelte';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	export let title = "";
	export let nextLink = "";
	export let prevLink = "";
	export let showNoContent = false;
	export let dialogFormPresent = true;
	export let buttonText = "Add New";
	export let valid = false;
	export let isNextDisabled = false;
	let isDialogActive = false;

	export const toggleDialog = () => {
		isDialogActive = !isDialogActive;
	}

	const formSubmit = () => {
		dispatch('submit');
	}
</script>

<Card class="vl-card pa-4 pa-md-8">
	<CardTitle class="pa-0">
		{title}
	</CardTitle>
	<CardText class="pa-0 mt-6 mb-6">
		<slot/>
		{#if showNoContent}
			<Alert class="error-color">
				<div>
					There is no {title}! Add one.
				</div>
			</Alert>
		{/if}
		{#if dialogFormPresent}
			<Button on:click={toggleDialog}>{buttonText}</Button>
			<Dialog persistent class="pa-8" bind:active={isDialogActive}>
				<form on:submit|preventDefault={formSubmit}>
					<slot name="dialog-form" />
					<Row class="ml-0 mr-0">
						<div style="flex:1; text-align: left;">
							<Button class="{valid ? 'success-color' : ''}" type="submit" disabled={!valid}>Create</Button>
						</div>
						<Button class="ml-3" type="reset">Reset</Button>
						<Button class="error-color  ml-3" on:click={toggleDialog}>Cancel</Button>
					</Row>
				</form>
			</Dialog>
		{/if}
	</CardText>
	<CardActions class="pa-0">
		<slot name="actions">
			<Row>
				{#if prevLink}
				<Col cols="6" class="text-left">
						<VlButton icon={mdiChevronLeft} text="Prev" href={prevLink} />
				</Col>
				{/if}
				{#if nextLink}
				<Col cols={prevLink ? 6 : 12} class="text-right">
					<VlButton icon={mdiChevronRight} text="Next" href={nextLink} disabled={isNextDisabled} />
				</Col>
				{/if}
			</Row>
		</slot>
	</CardActions>
</Card>

<style>
</style>