<svelte:head>
	<title>Fleet Crew</title>
</svelte:head>

<script>
	import CardForm from '../components/CardForm.svelte';
	import { Select } from 'svelte-materialify/src';
	import { Api } from "../api";
	import { inventories, flights, fleetcrews } from "../stores";
	import { required } from "../util/validators.js";

	let valid = false;

	let selectedAircraft = "";
	let selectedFlight = "";

	let formData = {
		aircraftId: "05e5b41c-1fc7-4946-b04a-fb7a43d9d119",
		carrier: "EMB",
		flightNumber: "1983",
		tailNumber: "2011"
	}

	let cardForm;

	const submit= async () => {
		const res = await Api.post("/fleetcrew/aircrafts/", formData);
		if (res) {
			$fleetcrews = [...$fleetcrews, res]
			cardForm.toggleDialog();
		}
	}

	$: aircrafts = $inventories.map((inv => {
		return {
			name: inv.manufacturerSpecification.manufacturer,
			value: inv.id,
			...inv
		};
	}));

	$: flightsForSelect = $flights.map((f => {
		return {
			name: f.id,
			value: f.id,
			...f
		};
	}));

	$: if (selectedAircraft) {
			const { id, carrier, registration } = aircrafts.find(airc => airc.id === selectedAircraft);
			formData.aircraftId = id;
			formData.carrier = carrier.name;
			formData.tailNumber = registration.tailNumber;
		}

	$: if (selectedFlight) {
			const f = flightsForSelect.find(flight => flight.id === selectedFlight);
			formData.flightNumber = f.id;
		}

	$: valid = !!selectedAircraft && !!selectedFlight;
</script>

<CardForm
	title="Fleet Crew"
	buttonText="New Fleet Crew"
	prevLink="flight-planning"
	nextLink="airport-terminal"
	isNextDisabled={$fleetcrews.length < 1}
	showNoContent={$fleetcrews.length < 1}
	{valid}
	on:submit={submit}
	bind:this={cardForm}
>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Departure Airport Code</th>
				<th>Arrival Airport Code</th>
				<th>Cancelled</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $fleetcrews as crew, ind (ind)}
				<tr>
					<td>{ind + 1}</td>
					<td>{crew}</td>
					<td>{crew}</td>
					<td>{crew}</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>

	<div slot="dialog-form">
		<Select rules={[required]} outlined items={aircrafts} bind:value={selectedAircraft}>Aircraft</Select>
		<Select rules={[required]} outlined items={flightsForSelect} bind:value={selectedFlight}>Flight</Select>
	</div>
</CardForm>
