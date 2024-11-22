package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import com.example.composecorelib.buttons.OptionActionView
import za.co.botcoin.R
import za.co.botcoin.enum.Status
import za.co.botcoin.services.BotService
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.KioskService
import za.co.botcoin.view.delayOnLifecycle
import za.co.botcoin.view.home.HomeTickerViewModel.Companion.DELAY
import za.co.botcoin.view.home.HomeTickerViewModel.Companion.PAIR_XRPZAR
import za.co.botcoin.view.settings.AutoTradeActivity

val COLLAPSED_TOP_BAR_HEIGHT = 56.dp
val EXPANDED_TOP_BAR_HEIGHT = 200.dp

class HomeTickerFragment : HomeBaseFragment() {
    private val homeTickerViewModel by viewModels<HomeTickerViewModel>(factoryProducer = { homeActivity.getViewModelFactory })
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                LargeTopBarWithItemList()
            }
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setUpMenu()
    fetchAndObserveTickers()
    navigateToPrivacyPolicyScreen(view)

    val delay: Long = 100000L
    handler.postDelayed(object : Runnable {
        override fun run() {
            fetchAndObserveTickers()
            if(!KioskService.isMyServiceRunning(homeActivity, BotService::class.java.simpleName)) {
                GeneralUtils.runAutoTrade(homeActivity)
            }
            handler.postDelayed(this, delay)
        }
    }, delay)

    /*binding.xrpZarLinearLayoutCompat.delayOnLifecycle(DELAY) {
            fetchAndObserveTickers()
            if(!KioskService.isMyServiceRunning(homeActivity, BotService::class.java.simpleName)) {
                GeneralUtils.runAutoTrade(homeActivity)
            }
        }*/
    }

    private fun setUpMenu() {
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }


            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.autoTrade -> {

                        //auto trade
                        startActivity(Intent(homeActivity, AutoTradeActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun fetchAndObserveTickers() {
        homeTickerViewModel.fetchTickers()
        homeTickerViewModel.tickersResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayCoins()

                        data.forEach { ticker ->
                            if (ticker.pair.equals(PAIR_XRPZAR, true)) {
                                this.binding.xrpZarOptionActionView.setText(getString(R.string.XRPZAR, ticker.lastTrade))
                            }
                        }
                    } else {
                        displayErrorMessage()
                    }
                }
                Status.ERROR -> {
                    displayErrorMessage()
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun displayCoins() {
        binding.xrpZarOptionActionView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        binding.xrpZarOptionActionView.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
    }

    private fun navigateToPrivacyPolicyScreen(view: View) {
        val privacyPolicyAcceptance = homeTickerViewModel.getPrivacyPolicyAcceptance()
        if (privacyPolicyAcceptance == null) {
            val action = HomeTickerFragmentDirections.actionHomeTickerFragmentToPrivacyPolicyFragment()
            Navigation.findNavController(view).navigate(action)
        } else {
            homeTickerViewModel.setUserTrailingStartPrice()
            homeTickerViewModel.setUserTrailingStopPrice()
            homeTickerViewModel.setSupportPriceCounter()
            homeTickerViewModel.setResistancePriceCounter()
            homeTickerViewModel.setSmartTrendDetectorMargin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(0)
    }*/
}

@Composable
private fun LargeTopBar(modifier: Modifier = Modifier, onClickFab: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier
            .background(colorResource(R.color.white))
            .fillMaxWidth()
            .height(EXPANDED_TOP_BAR_HEIGHT)
            .zIndex(1f),
        Alignment.BottomStart
    ) {
        Image(
            painterResource(R.drawable.space_rocket),
            "Rocket",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Text(
            "Library",
            Modifier.padding(16.dp),
            colorResource(R.color.colorPrimary),
            style = MaterialTheme.typography.titleLarge,
        )
        FloatingActionButton(
            { onClickFab() },
            Modifier.offset((screenWidth.minus(100.dp)), 30.dp),
            containerColor = colorResource(R.color.colorAccent),
        ) {
            Icon(Icons.Filled.Add, "Add")
        }
    }
}

@Composable
private fun CollapsedTopBar(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean
) {
    Box(
        modifier
            .background(colorResource(R.color.colorPrimary))
            .fillMaxWidth()
            .height(COLLAPSED_TOP_BAR_HEIGHT)
            .padding(16.dp)
            .zIndex(if (isCollapsed) 1f else 0f),
        Alignment.TopStart
    ) {
        AnimatedVisibility(isCollapsed) {
            Text("Library", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
@Preview
fun LargeTopBarWithItemList() {
    val listState = rememberLazyListState()

    val overlapHeightPx = with(LocalDensity.current) {
        EXPANDED_TOP_BAR_HEIGHT.toPx() - COLLAPSED_TOP_BAR_HEIGHT.toPx()
    }
    val isCollapsed: Boolean by remember {
        derivedStateOf {
            val isFirstItemHidden = listState.firstVisibleItemScrollOffset > overlapHeightPx
            isFirstItemHidden || listState.firstVisibleItemIndex > 0
        }
    }
    Box {
        CollapsedTopBar(isCollapsed = isCollapsed)
        LazyColumn(state = listState) {
            item { LargeTopBar { } }
            items(20) {
                OptionActionView(R.drawable.xrp, "XRP/ZAR") { }
            }
        }
    }
}